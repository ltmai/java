package mai.linh.junit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import mai.linh.junit.DateTimeUtils.IntervalType;

class DateTimeTest {

    @Test
    public void sameDate_createdDifferently_datesEqual() {
        var date1 = LocalDate.of(2020, 02, 20);        
        var date2 = LocalDate.parse("2020-02-20");

        assertEquals(date1, date2);
    }

    @Test
    public void today_addDays_getTomorrow() {
        var today = LocalDate.now();
        var tomorrow = today.plusDays(1);

        assertTrue((tomorrow.getDayOfYear() == today.getDayOfYear()+1) || 
                   (tomorrow.getYear() == today.getYear()+1));

        assertTrue(today.isBefore(tomorrow));
        assertTrue(tomorrow.isAfter(today));
    }

    @Test
    public void leapYear_checkLearYear_returnTrue() {
        assertTrue(LocalDate.of(2020, 02, 20).isLeapYear());
    }

    @Test
    public void boundaryFunctions_onLocalDateTime_returnBoundaryValues() {
        LocalDateTime beginningOfDay = LocalDate.now().atStartOfDay();
        LocalDate firstDayOfMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth());

        assertEquals(0, beginningOfDay.getHour());
        assertEquals(1, firstDayOfMonth.getDayOfMonth());
    }

    @ParameterizedTest(name = "Run #{index} : from {0} to {1} broken down in {2} month intervals")
    @CsvSource({
        // fromDate ,toDate     ,count  ,firstInterval                  ,lastInterval
        "2021-12-01 ,2022-03-01 ,4      ,From 2021-12-01 to 2021-12-31  ,From 2022-03-01 to 2022-03-01",
        "2022-12-01 ,2022-12-01 ,1      ,From 2022-12-01 to 2022-12-01  ,From 2022-12-01 to 2022-12-01",
        "2022-12-01 ,2022-12-31 ,1      ,From 2022-12-01 to 2022-12-31  ,From 2022-12-01 to 2022-12-31",
        "2022-03-10 ,2022-05-29 ,3      ,From 2022-03-10 to 2022-03-31  ,From 2022-05-01 to 2022-05-29",
    })
    public void dateRange_brokenDownInMonth_returnMonthIntervals(String from, String to, int count, String firstInterval, String lastInterval) {
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);
        List<DateTimeUtils.Interval> intervals = IntervalType.MONTH.breakDown(fromDate, toDate);
        List<String> intervalsAsString = intervals.stream().map(DateTimeUtils.Interval::toString).collect(Collectors.toList()); 

        assertEquals(count, intervalsAsString.size());
        assertEquals(firstInterval, intervalsAsString.get(0));
        assertEquals(lastInterval, intervalsAsString.get(intervalsAsString.size()-1));
    }

    @ParameterizedTest(name = "Run #{index} : from {0} to {1} broken down in {2} week intervals")
    @CsvSource({
        // fromDate ,toDate     ,count  ,firstInterval                  ,lastInterval
        "2022-12-01 ,2022-12-31 ,5      ,From 2022-12-01 to 2022-12-04  ,From 2022-12-26 to 2022-12-31",
        "2022-12-01 ,2022-12-01 ,1      ,From 2022-12-01 to 2022-12-01  ,From 2022-12-01 to 2022-12-01",
        "2022-12-05 ,2022-12-18 ,2      ,From 2022-12-05 to 2022-12-11  ,From 2022-12-12 to 2022-12-18",
        "2022-03-10 ,2022-05-29 ,12     ,From 2022-03-10 to 2022-03-13  ,From 2022-05-23 to 2022-05-29",
    })
    public void dateRange_brokenDownInWeek_returnWeekIntervals(String from, String to, int count, String firstInterval, String lastInterval) {
        LocalDate fromDate = LocalDate.parse(from);
        LocalDate toDate = LocalDate.parse(to);
        List<DateTimeUtils.Interval> intervals = IntervalType.WEEK.breakDown(fromDate, toDate);
        List<String> intervalsAsString = intervals.stream().map(DateTimeUtils.Interval::toString).collect(Collectors.toList());

        assertEquals(count, intervalsAsString.size());
        assertEquals(firstInterval, intervalsAsString.get(0));
        assertEquals(lastInterval, intervalsAsString.get(intervalsAsString.size()-1));
    }
}
