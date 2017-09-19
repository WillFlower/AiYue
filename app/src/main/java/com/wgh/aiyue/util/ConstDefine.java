package com.wgh.aiyue.util;


public class ConstDefine {

    public static final String DBNAME = "aiyue_db";
    public static final String LogFileName = "AiYueCrash.LOG";

    public static final double scalingValueScanActivity = 0.98;
    public static final int SubscriptionRecyclerViewColumnNum = 2;

    public static final String SpaceString = "";
    public static final String SplitString = ",";
    private static final String AppTheme = "appTheme";
    private static final String AppFirstTime = "appFirstTime";
    private static final String AppOfficalVersion = "appOfficalVersion";
    private static final String MailNum = "mailNum";
    private static final String MailNam = "mailNam";
    private static final String MailPas = "mailPas";
    private static final String MailNumTo = "mailNumTo";
    private static final String MailInterval = "mailInterval";
    private static final String RequestInterval = "requestInterval";
    private static final String LastMailTime = "lastMailTime";
    private static final String LastSearch = "lastSearch";
    private static final String LastRequestTime = "lastRequestTime";
    private static final String ApkVersion = "apkVersion";
    private static final String WebIp = "webIp";
    private static final String WebPort = "webPort";
    private static final String WebPath = "webPath";

    private static final String CategoryFirstKey = "categoryFirstKey";
    private static final String ViewPagerCategoryKey = "viewPagerCategoryKey";
    private static final String RightDrawerCategoryKey = "rightDrawerCategoryKey";
    private static final String LeftDrawerCategoryKey = "leftDrawerCategoryKey";
    private static final String RightDragCategoryKey = "rightDragCategoryKey";
    private static final String RightBelowCategoryKey = "rightBelowCategoryKey";
    private static final String AllFavoriteKey = "allFavoriteKey";
    private static final String FavoriteContentKey = "favoriteContentKey";
    public static final String ViewPagerColumnNumKey = "viewPagerColumnNumKey";

    public static final int ParseFail = 0;
    public static final int ParseSuccess = 1;

    public static final int DrawerCategoryChange = 0x00;
    public static final int PagerCategoryChange = 0x10;
    public static final int AppCrash = 0x20;
    public static final int PayOffical = 0x30;
    public static final int UserKey = 0x40;

    public static final int PGB_PER_REFRESH = 1;
    public static final int FINISH_DOWNLOAD = 2;

    public static boolean isFirstTime() {
        return (boolean) SPUtil.get(AppFirstTime, true);
    }

    public static boolean isOfficalVersion() {
        return (boolean) SPUtil.get(AppOfficalVersion, false);
    }

    public static void isOfficalVersion(boolean flag) {
        SPUtil.put(AppOfficalVersion, flag);
    }

    public static APPTheme getAppTheme() {
        return APPTheme.valueOf((String) SPUtil.get(AppTheme, APPTheme.WillFLow));
    }

    public static void setAppTheme(APPTheme appTheme) {
        SPUtil.put(AppTheme, appTheme.name());
    }

    public static String getMailNum() {
        return (String) SPUtil.get(MailNum, SpaceString);
    }

    public static String getMailNam() {
        return (String) SPUtil.get(MailNam, SpaceString);
    }

    public static String getMailPas() {
        return (String) SPUtil.get(MailPas, SpaceString);
    }

    public static String getMailNumTo() {
        return (String) SPUtil.get(MailNumTo, SpaceString);
    }

    public static String getViewPagerKey() {
        return (String) SPUtil.get(ViewPagerCategoryKey, SpaceString);
    }

    public static void setViewPagerKey(String viewPagerKey) {
        SPUtil.put(ViewPagerCategoryKey, viewPagerKey);
    }

    public static String getRightDrawerCategoryKey() {
        return (String) SPUtil.get(RightDrawerCategoryKey, SpaceString);
    }

    public static void setRightDrawerCategoryKey(String rightDrawerKey) {
        SPUtil.put(RightDrawerCategoryKey, rightDrawerKey);
    }

    public static int getLeftDrawerCategoryIndex() {
        return (int) SPUtil.get(LeftDrawerCategoryKey, 0);
    }

    public static void setLeftDrawerCategoryIndex(int index) {
        SPUtil.put(LeftDrawerCategoryKey, index);
    }

    public static void setRightChildCategoryList(String rightCategoryKey, String dragCategoriesJson, String belowCategoriesJson) {
        SPUtil.put(RightDragCategoryKey + rightCategoryKey, dragCategoriesJson);
        SPUtil.put(RightBelowCategoryKey + rightCategoryKey, belowCategoriesJson);
    }

    public static String getRightDragCategoryList(String rightCategoryKey) {
        return (String) SPUtil.get(RightDragCategoryKey + rightCategoryKey, SpaceString);
    }

    public static String getRightBelowCategoryList(String rightCategoryKey) {
        return (String) SPUtil.get(RightBelowCategoryKey + rightCategoryKey, SpaceString);
    }

    public static void setAllFavoriteKey(String key) {
        SPUtil.put(AllFavoriteKey, key);
    }

    public static String getAllFavoriteKey() {
        return (String) SPUtil.get(AllFavoriteKey, SpaceString);
    }

    public static void setFavoriteContents(String key, String favoriteContentJson) {
        SPUtil.put(FavoriteContentKey + key, favoriteContentJson);
    }

    public static String getFavoriteContents(String key) {
        return (String) SPUtil.get(FavoriteContentKey + key, SpaceString);
    }

    public static long getMailInterval() {
        return (long) SPUtil.get(MailInterval, (long) 10);
    }

    public static long getRequestInterval() {
        return (long) SPUtil.get(RequestInterval, (long) 10);
    }

    public static long getLastRequestTime() {
        return (long) SPUtil.get(LastRequestTime, (long) 0);
    }

    public static long getApkVersion() {
        return (long) SPUtil.get(ApkVersion, (long) 1);
    }

    public static String getWebPort() {
        return (String) SPUtil.get(WebPort, "8080");
    }

    public static String getWebIp() {
        return (String) SPUtil.get(WebIp, SpaceString);
    }

    public static String getWebPath() {
        return (String) SPUtil.get(WebPath, SpaceString);
    }

    public static void setLastMailTime(long lastMailTime) {
        SPUtil.put(LastMailTime, lastMailTime);
    }

    public static long getLastMailTime() {
        return (long) SPUtil.get(LastMailTime, (long) 0);
    }

    public static String getCategoryFirstKey() {
        return (String) SPUtil.get(CategoryFirstKey, SpaceString);
    }

    public static String getSearch() {
        return (String) SPUtil.get(LastSearch, SpaceString);
    }

    public static void setLastSearch(String string) {
        SPUtil.put(LastSearch, string);
    }

    public static int getViewPagerColumnNum() {
        return (int) SPUtil.get(ViewPagerColumnNumKey, 2);
    }

    public static void setViewPagerColumnNum(int columnNum) {
        SPUtil.put(ViewPagerColumnNumKey, columnNum);
    }
}