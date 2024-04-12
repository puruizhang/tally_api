package com.bestzpr.tally.service;

import com.bestzpr.tally.config.exception.JoinRoomException;
import com.bestzpr.tally.config.exception.NotFoundException;
import com.bestzpr.tally.config.exception.UnfinishedGameException;
import com.bestzpr.tally.dao.UserRoomDao;
import com.bestzpr.tally.domain.entity.Room;
import com.bestzpr.tally.domain.entity.User;
import com.bestzpr.tally.domain.entity.UserRoom;
import com.bestzpr.tally.domain.vo.GameStatsVO;
import com.bestzpr.tally.util.UserContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @className GameService
 * @Desc
 * @Author 张埔枘
 * @Date 2023/12/23 17:42
 * @Version 1.0
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class GameService {

    private final UserRoomDao userRoomDao;
    private final RoomService roomService;

    public Boolean joinRoom(Long roomId) {
        // 检查房间是否存在
        Room room = roomService.findById(roomId);
        if (Objects.isNull(room)) {
            throw new NotFoundException("房间不存在");
        }
        log.info("用户:{},加入房间:{}", UserContext.getUser().getUserName(), room.getId());

        // 检查下当前用户是否还有未结束的对局
        UserRoom userRoom = userRoomDao.findByUserIdAndStatus(UserContext.getUser().getId(), 0);
        if (null != userRoom) {
            throw new UnfinishedGameException("你还有对局未结束,请先结束对局再开始");
        }

        // 加入房间
        Boolean joinRoomBoolean = roomService.joinRoom(roomId);
        if (!joinRoomBoolean) {
            throw new JoinRoomException("加入房间失败");
        }
        return true;
    }

    public GameStatsVO getGameStatsVO() {
        User user = UserContext.getUser();
        // 赢x次 / 输x次 / 胜率70%
        List<UserRoom> userRoomList = userRoomDao.findListByUserIdAndStatus(user.getId(),1);
        // 按照 isVictory 属性分组，并计算每个分组的数量
        Map<Integer, Long> countByVictory = userRoomList.stream()
                .filter(userRoom -> userRoom.getVictoryFlag() != null)
                .collect(Collectors.groupingBy(UserRoom::getVictoryFlag, Collectors.counting()));
        // vo
        GameStatsVO gameStatsVO = new GameStatsVO();
        gameStatsVO.setUserName(user.getUserName());
        gameStatsVO.setUserAvatar(user.getAvatarUrl());
        gameStatsVO.setShowDisclaimer(false);
        gameStatsVO.setShowHistory(false);
        gameStatsVO.setLosses(!CollectionUtils.isEmpty(countByVictory) ? (null != countByVictory.get(0) ? countByVictory.get(0).intValue() : 0) :0 );
        gameStatsVO.setVictories(!CollectionUtils.isEmpty(countByVictory) ? (null != countByVictory.get(1) ? countByVictory.get(1).intValue() : 0) :0 );
        return gameStatsVO;
    }
}
