package br.ce.wcaquino.matchers;

import br.ce.wcaquino.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DayOfTheWeekMatcher extends TypeSafeMatcher<Date> {

    private final Integer dayOfWeek;

    public DayOfTheWeekMatcher(Integer dayOfWeek) {
        this.dayOfWeek = dayOfWeek;
    }

    @Override
    protected boolean matchesSafely(Date date) {
        return DataUtils.verificarDiaSemana(date, dayOfWeek);
    }

    @Override
    public void describeTo(Description description) {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_WEEK, dayOfWeek);
        String data = calendar.getDisplayName(Calendar.DAY_OF_WEEK, Calendar.LONG, new Locale("en", "US"));
        description.appendText(data);
    }

}
