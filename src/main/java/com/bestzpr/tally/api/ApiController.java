package com.bestzpr.tally.api;

import com.bestzpr.tally.dao.TransferDetailDao;
import com.bestzpr.tally.dao.UserDao;
import com.bestzpr.tally.dao.UserRoomDao;
import com.bestzpr.tally.domain.dto.WeChatUserInfoDTO;
import com.bestzpr.tally.domain.entity.*;
import com.bestzpr.tally.domain.req.TransferDetailReq;
import com.bestzpr.tally.domain.req.WxLoginRequest;
import com.bestzpr.tally.domain.vo.GameStatsVO;
import com.bestzpr.tally.domain.vo.RoomUserCase;
import com.bestzpr.tally.service.GameService;
import com.bestzpr.tally.service.RoomService;
import com.bestzpr.tally.service.TransferDetailService;
import com.bestzpr.tally.service.component.OSSService;
import com.bestzpr.tally.service.component.WechatService;
import com.bestzpr.tally.util.*;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @className ApiController
 * @Desc
 * @Author 张埔枘
 * @Date 2023/12/8 00:09
 * @Version 1.0
 */
@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/v1/api")
public class ApiController {

    private final UserDao userDao;
    private final UserRoomDao userRoomDao;
    private final RoomService roomService;
    private final OSSService ossService;
    private final WechatService wechatService;
    private final TransferDetailDao transferDetailDao;
    private final TransferDetailService transferDetailService;
    private final RedisUtil redisUtil;
    private final GameService gameService;

    @PostMapping("/joinRoom")
    public ResponseEntity joinRoom(@RequestParam Long roomId) {
        Boolean joinRoomBoolean = gameService.joinRoom(roomId);
        return ApiResponseUtil.createApiResponse(true,joinRoomBoolean);
    }

    @PostMapping("/userHistoryInfo")
    public ResponseEntity userHistoryInfo() {
        GameStatsVO gameStatsVO = gameService.getGameStatsVO();
        return ApiResponseUtil.createApiResponse(true,gameStatsVO);
    }

    @PostMapping("/getUserInfo")
    public ResponseEntity getUserInfo() {
        // 检查用户是否存在
        User user = UserContext.getUser();
        return ApiResponseUtil.createApiResponse(true,user);
    }

    @PostMapping("/getRoomInfo")
    public ResponseEntity getRoomInfo() {
        Room room = roomService.getCurrentRoomInfoByUser();
        return ApiResponseUtil.createApiResponse(true,room);
    }

    @PostMapping("/getOpenId")
    public ResponseEntity wxLogin(WxLoginRequest request) {
        User userExist = null;
        try {
            WeChatUserInfoDTO weChatLoginInfo = wechatService.getWeChatLoginInfo(request.getJsCode());
            // 返回成功响应
            // 注册一个用户
            userExist = userDao.findByOpenId(weChatLoginInfo.getOpenid());
            if(Objects.isNull(userExist)){
                userExist = new User();
                // 随机产生头像和名称
                userExist.setUserName(CartoonNameGenerator.generateCartoonName());
                userExist.setAvatarUrl(AvatarGenerator.getAvatarUrl());
                userExist.setOpenId(weChatLoginInfo.getOpenid());
                userExist.setCreateTime(new Date());
                userDao.save(userExist);
            }
        } catch (Exception ex) {
            log.info("wxLogin exception:{}", ex.getMessage());
        }
        return ApiResponseUtil.createApiResponse(true,userExist.getOpenId());
    }

    @PostMapping("/setUserAvatar")
    public ResponseEntity uploadFile(
            @RequestParam("userAvatar") MultipartFile file,
            @RequestHeader(value = "openId", required = false) String openId
    ) {
        // 处理文件上传逻辑
        if (!file.isEmpty()) {
            try {
                // 上传文件到阿里云OSS
                String fileUrl = ossService.uploadFile(file);
                ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(), "File uploaded successfully", fileUrl);
                User user = UserContext.getUser();
                if(null != user){
                    // 设置用户名
                    user.setAvatarUrl(fileUrl);
                    User save = userDao.save(user);
                }
                return ResponseEntity.ok().body(response);
            } catch (Exception e) {
                ApiResponse<String> response = new ApiResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Failed to upload file", null);
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
            }
        }

        ApiResponse<String> response = new ApiResponse<>(HttpStatus.BAD_REQUEST.value(), "No file provided", null);
        return ResponseEntity.badRequest().body(response);
    }

    @PostMapping("/setUserName")
    public ResponseEntity setUserName(@RequestParam String userName) {
        User user = UserContext.getUser();
        // 设置用户名
        user.setUserName(userName);
        User save = userDao.save(user);
        ApiResponse<User> response = new ApiResponse<User>(HttpStatus.OK.value(), "成功", save);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/buildRoom")
    public ResponseEntity buildRoom() {
        // 检查下当前用户是否还有未结束的对局
        UserRoom userRoom = userRoomDao.findByUserIdAndStatus(UserContext.getUser().getId(), 0);
        if(null != userRoom){
            ApiResponse<UserRoom> apiResponse = new ApiResponse<>(HttpStatus.OK.value(), "你还有对局未结束,请先结束对局再开始", userRoom);
            return ResponseEntity.ok().body(apiResponse);
        }
        Room room = roomService.buildRoom();
        ApiResponse<Room> response = new ApiResponse<Room>(HttpStatus.OK.value(), "成功", room);
        return ResponseEntity.ok().body(response);
    }


    @PostMapping("/roomTransferDetails")
    public ResponseEntity roomTransferDetails(Long roomId) {
        // 检查房间是否存在
        Room room = roomService.findById(roomId);
        if (room == null) {
            throw new IllegalArgumentException("房间不存在");
        }
        // 获取房间转账明细
        List<TransferDetail> transferDetailList = transferDetailDao.findByRoomIdOrderByCreateTimeDesc(roomId);
        // 返回房间转账明细
        ApiResponse<List<TransferDetail>> response = new ApiResponse<>(HttpStatus.OK.value(), "成功", transferDetailList);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/roomTransfer")
    public ResponseEntity<ApiResponse<TransferDetail>> roomTransfer(TransferDetailReq transferDetail) {
        // 检查房间是否存在
        Room room = roomService.findById(transferDetail.getRoomId());
        if (room == null) {
            throw new IllegalArgumentException("房间不存在");
        }

        // 检查用户是否存在
        transferDetail.setPayerUserId(UserContext.getUser().getId());
        Optional<User> payerUserOptional = userDao.findById(transferDetail.getPayerUserId());
        if (!payerUserOptional.isPresent()) {
            throw new IllegalArgumentException("付款人用户不存在");
        }
        // 检查用户是否存在
        Optional<User> payeeUserOptional = userDao.findById(transferDetail.getPayeeUserId());
        if (!payeeUserOptional.isPresent()) {
            throw new IllegalArgumentException("收款人用户不存在");
        }
        TransferDetail transferDetailSave = new TransferDetail();
        transferDetailSave.setRoomId(transferDetail.getRoomId());
        transferDetailSave.setPayerUser(payerUserOptional.get());
        transferDetailSave.setPayeeUser(payeeUserOptional.get());
        transferDetailSave.setAmount(transferDetail.getAmount());
        transferDetailSave.setCreateTime(new Date());

        // 执行转账
        TransferDetail save = transferDetailDao.save(transferDetailSave);
        ApiResponse<TransferDetail> response = new ApiResponse<TransferDetail>(HttpStatus.OK.value(), "成功", save);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping("/exitRoom")
    public ResponseEntity exitRoom(@RequestParam Long roomId) {
        // 检查房间是否存在
        Room room = roomService.findById(roomId);
        if (room == null) {
            throw new IllegalArgumentException("房间不存在");
        }

        // 退出房间,用户和房间的状态标识为结束
        UserRoom userRoom = userRoomDao.findByUserIdAndRoomId(UserContext.getUser().getId(), roomId);
        userRoom.setStatus(1);
        userRoom.setUpdateTime(new Date());
        // 结算胜负账单
        BigDecimal myAmount = transferDetailService.getTransferDetailsByRoomAndUser(roomId, UserContext.getUser().getId());
        if(null == myAmount){
            userRoom.setVictoryFlag(2);
        }else if(myAmount.compareTo(BigDecimal.ZERO) > 0){
            userRoom.setVictoryFlag(1);
        }else{
            userRoom.setVictoryFlag(0);
        }
        UserRoom saveAndFlush = userRoomDao.saveAndFlush(userRoom);
        // 用户退出房间后不再记住 他在这个房间的不展示房间二维码的设置
        String userRoomKey = UserContext.getUser().getId().toString()+roomId;
        redisUtil.delete(userRoomKey);
        ApiResponse<UserRoom> response = new ApiResponse<>(HttpStatus.OK.value(), "成功", saveAndFlush);
        return ResponseEntity.ok().body(response);
    }

    /**
     * 获取房间的相关信息
     * @param roomId
     */
    @PostMapping("/roomCommonInfo")
    public ResponseEntity<ApiResponse<RoomDetail>> roomCommonInfo(@RequestParam Long roomId){
        RoomDetail roomDetail = roomService.roomCommonInfo(roomId);
        ApiResponse<RoomDetail> response = new ApiResponse<>(HttpStatus.OK.value(), "成功", roomDetail);
        return ResponseEntity.ok().body(response);
    }

    @PostMapping(value = "/getRoomCode")
    public ResponseEntity<ApiResponse<String>> getRoomCode(@RequestParam Long roomId) {
        Room room = roomService.findById(roomId);
        ApiResponse<String> response = new ApiResponse<>(HttpStatus.OK.value(), "成功",room.getRoomCodeUrl());
        return ResponseEntity.ok().body(response);
    }

    /**
     * 获取当前用户所有经历过的房间的战绩结果
     */
    @PostMapping("/userHistoryInfoDetails")
    public ResponseEntity<ApiResponse<List<RoomUserCase>>> userHistoryInfoDetails(){
        List<RoomUserCase> roomUserCaseList = roomService.userHistoryInfoDetails();
        ApiResponse<List<RoomUserCase>> response = new ApiResponse<>(HttpStatus.OK.value(), "成功", roomUserCaseList);
        return ResponseEntity.ok().body(response);
    }

}
