package org.rent.app.utils;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ListIterator;
import java.util.Objects;
import java.util.stream.Collectors;
/**
 * DateIntervalProcessor
 * <p>
 *     Bunch  of common methods to work with collections of date intervals
 * </p>
 *
 * @author Sergey Yurkevich ysaspb@gmail.com
 * @since 24.06.2022
 */
public class DateIntervalProcessor {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private static final String delim = ";";
    private static final String NULL = "null";
    private static final String listDelim = ",";

    /**
     * Try to find interval from list which contains di and extract di from found interval.
     * @param list
     * @param di
     * @return true if the interval is found, false otherwise.
     */
    public static boolean reserve(final List<DateInterval> list, final DateInterval di) {
        Objects.requireNonNull(list);
        Objects.requireNonNull(di);
        ListIterator<DateInterval> listIterator = list.listIterator(list.size());
        boolean extracted = false;
        while (listIterator.hasPrevious()) {
            DateInterval left = listIterator.previous();
            if (Objects.isNull(left.getFromDate()) || !left.getFromDate().isAfter(di.getFromDate())) {
                if (Objects.isNull(left.getToDate()) || !left.getToDate().isBefore(di.getToDate())) {
                    if (Objects.equals(left.getFromDate(), di.getFromDate())) {
                        if (Objects.equals(left.getToDate(), di.getToDate())) {
                            listIterator.remove();
                        } else {
                            left.setFromDate(di.getToDate());
                        }
                    } else {
                        if (!Objects.equals(left.getToDate(), di.getToDate())) {
                            LocalDate oldFromDate = left.getFromDate();
                            left.setFromDate(di.getToDate());
                            listIterator.add(new DateInterval(oldFromDate, di.getToDate()));
                        } else {
                            left.setToDate(di.getFromDate());
                        }
                    }
                    extracted = true;
                } else {
                    break; // false
                }
            }
        }
        return extracted;
    }

    /**
     * Extend some interval from list with di.
     * @param list
     * @param di
     * @return changed list off intervals
     */
    public static List<DateInterval> release(List<DateInterval> list, DateInterval di) {
        Objects.requireNonNull(list);
        Objects.requireNonNull(di);
        ListIterator<DateInterval> listIterator = list.listIterator(list.size());
        List<DateInterval> res = null;
        DateInterval left = null;
        while (listIterator.hasPrevious()) {
            left = listIterator.previous();
            if (Objects.isNull(left.getFromDate()) || !left.getFromDate().isAfter(di.getFromDate())) {
                break;
            }
        }
        listIterator = list.listIterator(Objects.nonNull(left) ? list.indexOf(left) : 0);
        DateInterval right = null;
        while (listIterator.hasNext()) {
            right = listIterator.next();
            if (Objects.isNull(right.getToDate()) || !right.getToDate().isBefore(di.getToDate())) {
                break;
            }
        }
        if (Objects.isNull(left)) {
            if (Objects.isNull(right)) {
                res = new ArrayList<>(Collections.singleton(di));
            } else { // there is right
                if (Objects.isNull(di.getToDate())) {
                    res = new ArrayList<>(Collections.singleton(di));
                } else {
                    if (di.getToDate().isBefore(right.getFromDate())) {
                        res = new ArrayList<>(Collections.singleton(di));
                        res.addAll(list.subList(list.indexOf(right), list.size()));
                    } else {
                        di.setToDate(right.getToDate());
                        res = new ArrayList<>(Collections.singleton(di));
                        res.addAll(list.subList(list.indexOf(right) + 1, list.size()));
                    }
                }
            }
        } else { // there is left
            if (Objects.isNull(right)) {
                if (Objects.isNull(di.getFromDate())) {
                    res = new ArrayList<>(Collections.singleton(di));
                } else {
                    if (di.getFromDate().isAfter(left.getToDate())) {
                        res = new ArrayList<>(list.subList(0, list.indexOf(left) + 1));
                        res.add(di);
                    } else {
                        res = new ArrayList<>(list.subList(0, list.indexOf(left) + 1));
                        left.setToDate(di.getToDate());
                    }
                }
            } else { // there is left and right
                if (left.getToDate().isBefore(di.getFromDate())) {
                    if (di.getToDate().isBefore(right.getFromDate())) {
                        res = new ArrayList<>(list.subList(0, list.indexOf(left) + 1));
                        res.add(di);
                        res.addAll(list.subList(list.indexOf(right), list.size()));
                    } else {
                        right.setFromDate(di.getFromDate());
                        res = list;
                    }
                } else {
                    if (di.getToDate().isBefore(right.getFromDate())) {
                        res = new ArrayList<>(list.subList(0, list.indexOf(left) + 1));
                        left.setToDate(di.getToDate());
                        res.addAll(list.subList(list.indexOf(right), list.size()));

                    } else {
                        res = new ArrayList<>(list.subList(0, list.indexOf(left) + 1));
                        left.setToDate(right.getToDate());
                        res.addAll(list.subList(list.indexOf(right) + 1, list.size()));
                    }
                }
            }
        }
        return res;
    }

    /**
     * Serialize a date interval to string
     * @param di
     * @return
     */
    public static String convertDateIntervalToString(DateInterval di) {
        if (Objects.nonNull(di)) {
            LocalDate fromDate = di.getFromDate();
            String fromDateStr = Objects.nonNull(fromDate) ? formatter.format(fromDate) : NULL;
            LocalDate toDate = di.getToDate();
            String toDateStr = Objects.nonNull(toDate) ? formatter.format(toDate) : NULL;
            return fromDateStr + delim + toDateStr;
        } else {
            return null;
        }
    }

    /**
     * Deserialize a date interval from string
     * @param str
     * @return
     */
    public static DateInterval convertStringToDateInterval(String str) {
        if (Objects.nonNull(str)) {
            String[] parts = str.split(delim);
            LocalDate fromDate = (Objects.equals(NULL, parts[0])) ? null : LocalDate.parse(parts[0], formatter);
            LocalDate toDate = (Objects.equals(NULL, parts[1])) ? null : LocalDate.parse(parts[1], formatter);
            return new DateInterval(fromDate, toDate);
        } else {
            return null;
        }
    }

    /**
     * Deserialize list of date intervals from string
     * @param str
     * @return
     */
    public static List<DateInterval> convertStringToDateIntervalList(String str) {
        return Objects.nonNull(str) ? Arrays.stream(str.split(listDelim))
                .map(DateIntervalProcessor::convertStringToDateInterval)
                .collect(Collectors.toList())
                : Collections.emptyList();
    }

    /**
     * Serialize list of date intervals to string
     * @param list
     * @return
     */
    public static String convertDateIntervalListToString(List<DateInterval> list) {
        return Objects.nonNull(list) ?
                list.stream().map(DateIntervalProcessor::convertDateIntervalToString).collect(Collectors.joining(listDelim))
                : null;
    }
}
