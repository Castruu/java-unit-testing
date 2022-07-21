package br.ce.wcaquino.matchers;

import br.ce.wcaquino.utils.DataUtils;

import java.util.Calendar;
import java.util.Date;

public class LocationMatchers {

    public static DayOfTheWeekMatcher isA(Integer integer) {
        return new DayOfTheWeekMatcher(integer);
    }

    public static DayOfTheWeekMatcher isAMonday() {
        return new DayOfTheWeekMatcher(Calendar.MONDAY);
    }

    public static DayMatcher isToday() {
        return new DayMatcher(new Date());
    }

    public static DayMatcher isTodayWithDaysDifference(Integer integer) {
        return new DayMatcher(DataUtils.obterDataComDiferencaDias(integer));
    }

}
