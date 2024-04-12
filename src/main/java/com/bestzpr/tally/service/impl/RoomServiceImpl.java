package com.bestzpr.tally.service.impl;

import com.bestzpr.tally.dao.RoomDao;
import com.bestzpr.tally.dao.UserDao;
import com.bestzpr.tally.dao.UserRoomDao;
import com.bestzpr.tally.domain.entity.*;
import com.bestzpr.tally.domain.vo.RoomUserCase;
import com.bestzpr.tally.service.RoomService;
import com.bestzpr.tally.service.TransferDetailService;
import com.bestzpr.tally.service.component.WechatService;
import com.bestzpr.tally.util.DateUtil;
import com.bestzpr.tally.util.RedisUtil;
import com.bestzpr.tally.util.UserContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;

/**
 * @className RoomServiceImpl
 * @Desc
 * @Author 张埔枘
 * @Date 2023/12/12 21:22
 * @Version 1.0
 */
@RequiredArgsConstructor
@Service
public class RoomServiceImpl implements RoomService {

    private final RoomDao roomDao;
    private final UserDao userDao;
    private final UserRoomDao userRoomDao;
    private final TransferDetailService transferDetailService;
    private final RedisUtil redisUtil;
    private final WechatService wechatService;

    @Transactional(rollbackFor = Exception.class)
    @Override
    public Room buildRoom() {
        // 创建新的房间信息
        Room room = new Room();
        room.setRoomStatus(0);
        room.setOwnerId(UserContext.getUser().getId());
        room.setCreateTime(new Date());
        roomDao.save(room);
        room.setRoomCodeUrl(wechatService.getQRCodeUrl(room.getId()));
        roomDao.saveAndFlush(room);
        // 绑定当前用户与房间
        UserRoom userRoom = new UserRoom();
        userRoom.setUser(UserContext.getUser());
        userRoom.setRoom(room);
        userRoom.setStatus(0);
        userRoom.setCreateTime(new Date());
        userRoomDao.save(userRoom);
        return room;
    }

    @Override
    public Room findById(Long roomId) {
        Optional<Room> optionalRoom = roomDao.findById(roomId);
        return optionalRoom.orElse(null);
    }

    @Override
    public Boolean joinRoom(Long roomId) {
        Optional<Room> optionalRoom = roomDao.findById(roomId);
        if(!optionalRoom.isPresent()){
            throw new RuntimeException("房间不存在");
        }
        // 判断当前用户是否已经绑定过该房间了
        UserRoom userRoomExist = userRoomDao.findByUserIdAndRoomId(UserContext.getUser().getId(),optionalRoom.get().getId());
        if(null == userRoomExist){
            // 绑定当前用户和这个房间
            UserRoom userRoom = new UserRoom();
            userRoom.setUser(UserContext.getUser());
            userRoom.setRoom(optionalRoom.get());
            userRoom.setStatus(0);
            userRoom.setCreateTime(new Date());
            UserRoom save = userRoomDao.save(userRoom);
        }else{
            userRoomExist.setStatus(0);
            userRoomExist.setVictoryFlag(null);
            userRoomExist.setUpdateTime(new Date());
            userRoomDao.saveAndFlush(userRoomExist);
        }
        return true;
    }

    @Override
    public RoomDetail roomCommonInfo(Long roomId) {
        String userRoomKey = UserContext.getUser().getId().toString()+roomId;
        Optional<Room> roomOptional = roomDao.findById(roomId);
        if(roomOptional.isEmpty()){
            throw new RuntimeException("房间不存在");
        }
        User user = UserContext.getUser();

        BigDecimal myAmount = transferDetailService.getTransferDetailsByRoomAndUser(roomId, user.getId());

        RoomDetail roomDetail = new RoomDetail();
        Room room = roomOptional.get();
        roomDetail.setRoomId(room.getId().toString());
        roomDetail.setRoomNo(room.getId().toString());
        roomDetail.setRoomCode(room.getId().toString());
        roomDetail.setRoomCode(room.getRoomCodeUrl());
        RoomDetail.MyPointInfoVo myPointInfoVo = new RoomDetail.MyPointInfoVo();
        // 根据转账表记录计算得出
        myPointInfoVo.setUserPoint(null != myAmount ? myAmount : BigDecimal.ZERO);
        myPointInfoVo.setUserId(user.getId());
        myPointInfoVo.setUserAvatar(user.getAvatarUrl());
        myPointInfoVo.setUserName(user.getUserName());
        roomDetail.setMyPointInfoVo(myPointInfoVo);
        // 当前房间的所有用户的当前积分情况
        roomDetail.setOtherPointInfoVoArr(getUserRoomByRoomId(roomId));
        if(Objects.isNull(redisUtil.get(userRoomKey))){
            roomDetail.setShowGuide(true);
            roomDetail.setShowCode(true);
        }
        redisUtil.set(userRoomKey,1);
//        RoomDetail.Option option = new RoomDetail.Option();
//        option.setName("");
//        option.setIcon("");
//        option.setOpenType("");
        return roomDetail;
    }

    @Override
    public List<RoomDetail.OtherPointInfoVo> getUserRoomByRoomId(Long roomId) {
        // 查询出未结束的房间绑定的用户信息
        List<UserRoom> userRooms = userRoomDao.findByRoomIdAndStatus(roomId,0);
        List<RoomDetail.OtherPointInfoVo> otherPointInfoVoArrayList = new ArrayList<>();
        for (UserRoom userRoom : userRooms) {
            User user = userRoom.getUser();
            if(user.getId().equals(UserContext.getUser().getId())){
                continue;
            }
            BigDecimal decimal = transferDetailService.getTransferDetailsByRoomAndUser(roomId, user.getId());
            // 计算用户的积分信息
//            Integer score = calculateScore(transferDetails);
            // 在这里处理积分信息，可以打印、存储到列表中等等
            RoomDetail.OtherPointInfoVo otherPointInfoVo = new RoomDetail.OtherPointInfoVo();
            otherPointInfoVo.setUserPoint(null == decimal ? BigDecimal.ZERO : decimal);
            otherPointInfoVo.setUserAvatar(user.getAvatarUrl());
            otherPointInfoVo.setUserName(user.getUserName());
            otherPointInfoVo.setUserId(user.getId());
            otherPointInfoVoArrayList.add(otherPointInfoVo);
        }
        return otherPointInfoVoArrayList;
    }

    @Override
    public List<RoomUserCase> userHistoryInfoDetails() {
        List<RoomUserCase> caseArrayList = new ArrayList<>();
        // 查询当前用户所有房间的战绩情况
        List<UserRoom> userRoomList = userRoomDao.findByUserId(UserContext.getUser().getId());
        for (UserRoom userRoom : userRoomList) {
            RoomUserCase roomUserCase = new RoomUserCase();
            BigDecimal userPoint = transferDetailService.getTransferDetailsByRoomAndUser(userRoom.getRoom().getId(), UserContext.getUser().getId());
            Optional<User> optional = userDao.findById(userRoom.getRoom().getOwnerId());
            roomUserCase.setRoomName(optional.get().getUserName());
            roomUserCase.setUserPoint(Objects.isNull(userPoint) ? BigDecimal.ZERO : userPoint);
            roomUserCase.setCreateTime(DateUtil.formatDateTime(userRoom.getRoom().getCreateTime()));
            caseArrayList.add(roomUserCase);
        }
        return caseArrayList;
    }

    @Override
    public Room getCurrentRoomInfoByUser() {
        // 查询当前有关系的房间
        // 检查房间是否存在
        User user = UserContext.getUser();
        UserRoom userRoom = userRoomDao.findByUserIdAndStatus(user.getId(),0);
        if (userRoom == null) {
           return null;
        }
        return findById(userRoom.getRoom().getId());
    }

    private Integer calculateScore(List<TransferDetail> transferDetails) {
        // 根据支付情况计算用户的积分信息
        Integer score = 0;
        for (TransferDetail transferDetail : transferDetails) {
            // 进行积分计算的逻辑
            // 例如，根据支付金额进行累加
            score += transferDetail.getAmount().intValue();
        }
        return score;
    }
}
