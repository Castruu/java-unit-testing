package br.ce.wcaquino.matchers;

import br.ce.wcaquino.utils.DataUtils;
import org.hamcrest.Description;
import org.hamcrest.TypeSafeMatcher;

import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DayMatcher extends TypeSafeMatcher<Date> {

    private final Date expectedDate;

    public DayMatcher(Date expectedDate) {
        this.expectedDate = expectedDate;
    }

    @Override
    protected boolean matchesSafely(Date currentDate) {
        return DataUtils.isMesmaData(expectedDate, currentDate);
    }

    @Override
    public void describeTo(Description description) {
        description.appendText(expectedDate.toString());
    }
}
