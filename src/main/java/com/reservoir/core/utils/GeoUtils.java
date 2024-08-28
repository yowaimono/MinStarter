package com.reservoir.core.utils;

import java.util.List;

public class GeoUtils {

    private static final double EARTH_RADIUS = 6371000; // 地球半径，单位：米

    /**
     * 计算两点之间的距离（单位：米）
     *
     * @param lat1 第一个点的纬度
     * @param lon1 第一个点的经度
     * @param lat2 第二个点的纬度
     * @param lon2 第二个点的经度
     * @return 两点之间的距离（单位：米）
     */
    public static double distance(double lat1, double lon1, double lat2, double lon2) {
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return EARTH_RADIUS * c;
    }

    /**
     * 判断一个点是否在一个多边形内
     *
     * @param pointLat 点的纬度
     * @param pointLon 点的经度
     * @param polygon  多边形的顶点列表，每个顶点是一个数组 [纬度, 经度]
     * @return 如果点在多边形内，返回 true；否则返回 false
     */
    public static boolean isPointInPolygon(double pointLat, double pointLon, List<double[]> polygon) {
        int intersectCount = 0;
        for (int i = 0; i < polygon.size(); i++) {
            double[] p1 = polygon.get(i);
            double[] p2 = polygon.get((i + 1) % polygon.size());
            if (isIntersect(pointLat, pointLon, p1[0], p1[1], p2[0], p2[1])) {
                intersectCount++;
            }
        }
        return (intersectCount % 2) == 1;
    }

    private static boolean isIntersect(double pointLat, double pointLon, double lat1, double lon1, double lat2, double lon2) {
        if (lon1 == lon2) {
            return pointLon == lon1 && pointLat >= Math.min(lat1, lat2) && pointLat <= Math.max(lat1, lat2);
        }
        if (lon1 > lon2) {
            double tempLat = lat1;
            double tempLon = lon1;
            lat1 = lat2;
            lon1 = lon2;
            lat2 = tempLat;
            lon2 = tempLon;
        }
        if (pointLon < lon1 || pointLon > lon2) {
            return false;
        }
        double x = (pointLon - lon1) * (lat2 - lat1) / (lon2 - lon1) + lat1;
        return x > pointLat;
    }



    public static int[] decimalDegreesToDMS(double decimalDegrees) {
        int degrees = (int) decimalDegrees;
        double fractionalDegrees = Math.abs(decimalDegrees - degrees);
        int minutes = (int) (fractionalDegrees * 60);
        double fractionalMinutes = fractionalDegrees * 60 - minutes;
        int seconds = (int) Math.round(fractionalMinutes * 60);
        if (seconds == 60) {
            minutes++;
            seconds = 0;
        }
        if (minutes == 60) {
            degrees++;
            minutes = 0;
        }
        return new int[]{degrees, minutes, seconds};
    }

    public static double dmsToDecimalDegrees(int degrees, int minutes, double seconds) {
        double decimalDegrees = degrees + (minutes / 60.0) + (seconds / 3600.0);
        return decimalDegrees;
    }


//    public static void main(String[] args) {
//        // 28°41'57"N 116°01'20"E
//        Double x1 = dmsToDecimalDegrees(28, 41, 57);
//        System.out.println(x1);
//        Double y1 = dmsToDecimalDegrees(116, 01, 20);
//        System.out.println(y1);
//
//        // 28°41'47"N 116°01'54"E
//        Double x2 = dmsToDecimalDegrees(28, 41, 47);
//        System.out.println(x2);
//        Double y2 = dmsToDecimalDegrees(116, 01, 54);
//        System.out.println(y2);
//
//        // 28°41'14"N 116°01'57"E
//        Double x3 = dmsToDecimalDegrees(28, 41, 14);
//        System.out.println(x3);
//        Double y3 = dmsToDecimalDegrees(116, 01, 57);
//        System.out.println(y3);
//
//        // 28°41'18"N 116°01'06"E
//        Double x4 = dmsToDecimalDegrees(28, 41, 18);
//        System.out.println(x4);
//        Double y4 = dmsToDecimalDegrees(116, 01, 06);
//        System.out.println(y4);
//
//        //28°41'47"N 116°01'05"E
//        Double x5 = dmsToDecimalDegrees(28, 41, 47);
//        System.out.println(x5);
//        Double y5 = dmsToDecimalDegrees(116, 01, 05);
//        System.out.println(y5);
//
//
//        List<double[]> polygon = List.of(
//                new double[]{x1,y1},
//                new double[]{x2,y2},
//                new double[]{x3,y3},
//                new double[]{x4,y4}
//        );
//        boolean isInside = GeoUtils.isPointInPolygon(x5,y5, polygon);
//        System.out.println("Is point inside polygon: " + isInside);
//
//
//
//
//
//
//    }
}