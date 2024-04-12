package com.bestzpr.tally.domain.vo;

import lombok.Data;

import java.text.DecimalFormat;

/**
 * @className GameStatsVO
 * @Desc 个人游戏信息
 * @Author 张埔枘
 * @Date 2023/12/17 23:43
 * @Version 1.0
 */
@Data
public class GameStatsVO {

    private String userName;
    private String userAvatar;
    private boolean showDisclaimer;
    private boolean showHistory;

    //
    private int victories;
    private int losses;
    // 胜率
    private String winRate;

    public void setLosses(int losses) {
        this.losses = losses;
        this.winRate = calculateWinRate();
    }

    public void setVictories(int victories) {
        this.victories = victories;
        this.winRate = calculateWinRate();
    }

    private String calculateWinRate() {
        double totalGames = victories + losses;
        if (totalGames == 0) {
            return "0%";
        }
        double winRate = (victories / totalGames) * 100;
        DecimalFormat decimalFormat = new DecimalFormat("0.00");
        return decimalFormat.format(winRate) + "%";
    }
}
