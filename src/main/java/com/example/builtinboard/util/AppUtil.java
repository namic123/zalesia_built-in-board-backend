package com.example.builtinboard.util;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class AppUtil {
    // 날짜 형식 변환 유틸리티
    public static String getAgo(LocalDateTime insertedTime) {

        LocalDateTime nowTime = LocalDateTime.now(ZoneId.of("Asia/Seoul"));

        // inserted가 now와 비교했을 때 1년 이상인지 확인
        if (insertedTime.isBefore(nowTime.minusYears(1))) {
            // inserted 날짜와 현재 날짜 'nowTime'사이의 시간 차이를 계산
            Period between = Period.between(insertedTime.toLocalDate(), nowTime.toLocalDate());
            // 위 날짜 간 연도차이를 return
            return between.get(ChronoUnit.YEARS) + "년 전";
        }
        // Month 계산
        else if (insertedTime.isBefore(nowTime.minusMonths(1))) {
            Period between = Period.between(insertedTime.toLocalDate(), nowTime.toLocalDate());
            return between.get(ChronoUnit.MONTHS) + "달 전";
        } // Day 계산
        else if (insertedTime.isBefore(nowTime.minusDays(1))) {
            Period between = Period.between(insertedTime.toLocalDate(), nowTime.toLocalDate());
            return between.get(ChronoUnit.DAYS) + "일 전";
        } // Hour 계산
        else if (insertedTime.isBefore(nowTime.minusHours(1))) {
            // inserted의 시간과 now 사이의 차이를 계산
            Duration betweenTime = Duration.between(insertedTime, nowTime);
            return (betweenTime.getSeconds() / 60 / 60) + "시간 전";
        }// Minute 계산
        else if (insertedTime.isBefore(nowTime.minusMinutes(1))) {
            Duration betweenTime = Duration.between(insertedTime, nowTime);
            return (betweenTime.getSeconds() / 60) + "분 전";
        }// Second 계산
        else {
            Duration betweenTime = Duration.between(insertedTime, nowTime);
            return (betweenTime.getSeconds()) + "초 전";
        }
    }
}
