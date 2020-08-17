package com.kmcj.karte.resources.sigma.log;

import com.kmcj.karte.BasicResponse;

/**
 *
 * @author admin
 */
public class TblSigmaLogVo extends BasicResponse {
    private String machineId;

    private String machineName;
    
    private String machineUuid;

    private String eventNo;

    private String createDate;

    private String info01;

    private String info02;

    private String info03;

    private String info04;

    private String info05;

    private String info06;

    private String info07;

    private String info08;

    private String info09;

    private String info10;

    private String info11;

    private String info12;

    private String info13;

    private String info14;

    private String info15;

    private String info16;

    private String info17;

    private String info18;

    private String info19;

    private String info20;

    private String info21;

    private String info22;

    private String info23;

    private String info24;

    private String info25;

    private String info26;

    private String info27;

    private String info28;

    private String info29;

    private String info30;

    private String info31;

    private String info32;

    private String col01;

    private String col02;

    private String col03;

    private String col04;

    private String col05;

    private String col06;

    private String col07;

    private String col08;

    private String col09;

    private String col10;

    private String col11;

    private String col12;

    private String col13;

    private String col14;

    private String col15;

    private String col16;

    private String col17;

    private String col18;

    private String col19;

    private String col20;

    private String col21;

    private String col22;

    private String col23;

    private String col24;

    private String col25;

    private String col26;

    private String col27;

    private String col28;

    private String col29;

    private String col30;

    private String col31;

    private String col32;
    
//    private String log001;
//
//    private String log002;
//
//    private String log003;
//
//    private String log004;
//
//    private String log005;
//
//    private String log006;
//
//    private String log007;
//
//    private String log008;
//
//    private String log009;
//
//    private String log010;
//
//    private String log011;
//
//    private String log012;
//
//    private String log013;
//
//    private String log014;
//
//    private String log015;
//
//    private String log016;
//
//    private String log017;
//
//    private String log018;
//
//    private String log019;
//
//    private String log020;
//
//    private String log021;
//
//    private String log022;
//
//    private String log023;
//
//    private String log024;
//
//    private String log025;
//
//    private String log026;
//
//    private String log027;
//
//    private String log028;
//
//    private String log029;
//
//    private String log030;
//
//    private String log031;
//
//    private String log032;
//
//    private String log033;
//
//    private String log034;
//
//    private String log035;
//
//    private String log036;
//
//    private String log037;
//
//    private String log038;
//
//    private String log039;
//
//    private String log040;
//
//    private String log041;
//
//    private String log042;
//
//    private String log043;
//
//    private String log044;
//
//    private String log045;
//
//    private String log046;
//
//    private String log047;
//
//    private String log048;
//
//    private String log049;
//
//    private String log050;
//
//    private String log051;
//
//    private String log052;
//
//    private String log053;
//
//    private String log054;
//
//    private String log055;
//
//    private String log056;
//
//    private String log057;
//
//    private String log058;
//
//    private String log059;
//
//    private String log060;
//
//    private String log061;
//
//    private String log062;
//
//    private String log063;
//
//    private String log064;
//
//    private String log065;
//
//    private String log066;
//
//    private String log067;
//
//    private String log068;
//
//    private String log069;
//
//    private String log070;
//
//    private String log071;
//
//    private String log072;
//
//    private String log073;
//
//    private String log074;
//
//    private String log075;
//
//    private String log076;
//
//    private String log077;
//
//    private String log078;
//
//    private String log079;
//
//    private String log080;
//
//    private String log081;
//
//    private String log082;
//
//    private String log083;
//
//    private String log084;
//
//    private String log085;
//
//    private String log086;
//
//    private String log087;
//
//    private String log088;
//
//    private String log089;
//
//    private String log090;
//
//    private String log091;
//
//    private String log092;
//
//    private String log093;
//
//    private String log094;
//
//    private String log095;
//
//    private String log096;
//
//    private String log097;
//
//    private String log098;
//
//    private String log099;
//
//    private String log100;
//
//    private String log101;
//
//    private String log102;
//
//    private String log103;
//
//    private String log104;
//
//    private String log105;
//
//    private String log106;
//
//    private String log107;
//
//    private String log108;
//
//    private String log109;
//
//    private String log110;
//
//    private String log111;
//
//    private String log112;
//
//    private String log113;
//
//    private String log114;
//
//    private String log115;
//
//    private String log116;
//
//    private String log117;
//
//    private String log118;
//
//    private String log119;
//
//    private String log120;
//
//    private String log121;
//
//    private String log122;
//
//    private String log123;
//
//    private String log124;
//
//    private String log125;
//
//    private String log126;
//
//    private String log127;
//
//    private String log128;
//
//    private String log129;
//
//    private String log130;
//
//    private String log131;
//
//    private String log132;
//
//    private String log133;
//
//    private String log134;
//
//    private String log135;
//
//    private String log136;
//
//    private String log137;
//
//    private String log138;
//
//    private String log139;
//
//    private String log140;
//
//    private String log141;
//
//    private String log142;
//
//    private String log143;
//
//    private String log144;
//
//    private String log145;
//
//    private String log146;
//
//    private String log147;
//
//    private String log148;
//
//    private String log149;
//
//    private String log150;
//
//    private String log151;
//
//    private String log152;
//
//    private String log153;
//
//    private String log154;
//
//    private String log155;
//
//    private String log156;
//
//    private String log157;
//
//    private String log158;
//
//    private String log159;
//
//    private String log160;
//
//    private String log161;
//
//    private String log162;
//
//    private String log163;
//
//    private String log164;
//
//    private String log165;
//
//    private String log166;
//
//    private String log167;
//
//    private String log168;
//
//    private String log169;
//
//    private String log170;
//
//    private String log171;
//
//    private String log172;
//
//    private String log173;
//
//    private String log174;
//
//    private String log175;
//
//    private String log176;
//
//    private String log177;
//
//    private String log178;
//
//    private String log179;
//
//    private String log180;
//
//    private String log181;
//
//    private String log182;
//
//    private String log183;
//
//    private String log184;
//
//    private String log185;
//
//    private String log186;
//
//    private String log187;
//
//    private String log188;
//
//    private String log189;
//
//    private String log190;
//
//    private String log191;
//
//    private String log192;

    private String power;

    private String autoMode;

    private String cycleStart;

    private String alert;

    private String alertInfo;

    private String shotCnt;

    private String cycleTime;

    private String weighingTime;

    private String minCushionPos;

    private String fillingPeak;

    private String maxPressure;
    
    private TblSigmaLog tblSigmaLog;

    public TblSigmaLogVo() {
    }

    public String getMachineName() {
        return machineName;
    }

    public String getEventNo() {
        return eventNo;
    }

    public String getCreateDate() {
        return createDate;
    }

    public String getInfo01() {
        return info01;
    }

    public String getInfo02() {
        return info02;
    }

    public String getInfo03() {
        return info03;
    }

    public String getInfo04() {
        return info04;
    }

    public String getInfo05() {
        return info05;
    }

    public String getInfo06() {
        return info06;
    }

    public String getInfo07() {
        return info07;
    }

    public String getInfo08() {
        return info08;
    }

    public String getInfo09() {
        return info09;
    }

    public String getInfo10() {
        return info10;
    }

    public String getInfo11() {
        return info11;
    }

    public String getInfo12() {
        return info12;
    }

    public String getInfo13() {
        return info13;
    }

    public String getInfo14() {
        return info14;
    }

    public String getInfo15() {
        return info15;
    }

    public String getInfo16() {
        return info16;
    }

    public String getInfo17() {
        return info17;
    }

    public String getInfo18() {
        return info18;
    }

    public String getInfo19() {
        return info19;
    }

    public String getInfo20() {
        return info20;
    }

    public String getInfo21() {
        return info21;
    }

    public String getInfo22() {
        return info22;
    }

    public String getInfo23() {
        return info23;
    }

    public String getInfo24() {
        return info24;
    }

    public String getInfo25() {
        return info25;
    }

    public String getInfo26() {
        return info26;
    }

    public String getInfo27() {
        return info27;
    }

    public String getInfo28() {
        return info28;
    }

    public String getInfo29() {
        return info29;
    }

    public String getInfo30() {
        return info30;
    }

    public String getInfo31() {
        return info31;
    }

    public String getInfo32() {
        return info32;
    }

    public String getCol01() {
        return col01;
    }

    public String getCol02() {
        return col02;
    }

    public String getCol03() {
        return col03;
    }

    public String getCol04() {
        return col04;
    }

    public String getCol05() {
        return col05;
    }

    public String getCol06() {
        return col06;
    }

    public String getCol07() {
        return col07;
    }

    public String getCol08() {
        return col08;
    }

    public String getCol09() {
        return col09;
    }

    public String getCol10() {
        return col10;
    }

    public String getCol11() {
        return col11;
    }

    public String getCol12() {
        return col12;
    }

    public String getCol13() {
        return col13;
    }

    public String getCol14() {
        return col14;
    }

    public String getCol15() {
        return col15;
    }

    public String getCol16() {
        return col16;
    }

    public String getCol17() {
        return col17;
    }

    public String getCol18() {
        return col18;
    }

    public String getCol19() {
        return col19;
    }

    public String getCol20() {
        return col20;
    }

    public String getCol21() {
        return col21;
    }

    public String getCol22() {
        return col22;
    }

    public String getCol23() {
        return col23;
    }

    public String getCol24() {
        return col24;
    }

    public String getCol25() {
        return col25;
    }

    public String getCol26() {
        return col26;
    }

    public String getCol27() {
        return col27;
    }

    public String getCol28() {
        return col28;
    }

    public String getCol29() {
        return col29;
    }

    public String getCol30() {
        return col30;
    }

    public String getCol31() {
        return col31;
    }

    public String getCol32() {
        return col32;
    }
    
//    public String getLog001() {	
//        return log001;	
//    }
//
//    public String getLog002() {	
//        return log002;	
//    }
//
//    public String getLog003() {	
//        return log003;	
//    }
//
//    public String getLog004() {	
//        return log004;	
//    }
//
//    public String getLog005() {	
//        return log005;	
//    }
//
//    public String getLog006() {	
//        return log006;	
//    }
//
//    public String getLog007() {	
//        return log007;	
//    }
//
//    public String getLog008() {	
//        return log008;	
//    }
//
//    public String getLog009() {	
//        return log009;	
//    }
//
//    public String getLog010() {	
//        return log010;	
//    }
//
//    public String getLog011() {	
//        return log011;	
//    }
//
//    public String getLog012() {	
//        return log012;	
//    }
//
//    public String getLog013() {	
//        return log013;	
//    }
//
//    public String getLog014() {	
//        return log014;	
//    }
//
//    public String getLog015() {	
//        return log015;	
//    }
//
//    public String getLog016() {	
//        return log016;	
//    }
//
//    public String getLog017() {	
//        return log017;	
//    }
//
//    public String getLog018() {	
//        return log018;	
//    }
//
//    public String getLog019() {	
//        return log019;	
//    }
//
//    public String getLog020() {	
//        return log020;	
//    }
//
//    public String getLog021() {	
//        return log021;	
//    }
//
//    public String getLog022() {	
//        return log022;	
//    }
//
//    public String getLog023() {	
//        return log023;	
//    }
//
//    public String getLog024() {	
//        return log024;	
//    }
//
//    public String getLog025() {	
//        return log025;	
//    }
//
//    public String getLog026() {	
//        return log026;	
//    }
//
//    public String getLog027() {	
//        return log027;	
//    }
//
//    public String getLog028() {	
//        return log028;	
//    }
//
//    public String getLog029() {	
//        return log029;	
//    }
//
//    public String getLog030() {	
//        return log030;	
//    }
//
//    public String getLog031() {	
//        return log031;	
//    }
//
//    public String getLog032() {	
//        return log032;	
//    }
//
//    public String getLog033() {	
//        return log033;	
//    }
//
//    public String getLog034() {	
//        return log034;	
//    }
//
//    public String getLog035() {	
//        return log035;	
//    }
//
//    public String getLog036() {	
//        return log036;	
//    }
//
//    public String getLog037() {	
//        return log037;	
//    }
//
//    public String getLog038() {	
//        return log038;	
//    }
//
//    public String getLog039() {	
//        return log039;	
//    }
//
//    public String getLog040() {	
//        return log040;	
//    }
//
//    public String getLog041() {	
//        return log041;	
//    }
//
//    public String getLog042() {	
//        return log042;	
//    }
//
//    public String getLog043() {	
//        return log043;	
//    }
//
//    public String getLog044() {	
//        return log044;	
//    }
//
//    public String getLog045() {	
//        return log045;	
//    }
//
//    public String getLog046() {	
//        return log046;	
//    }
//
//    public String getLog047() {	
//        return log047;	
//    }
//
//    public String getLog048() {	
//        return log048;	
//    }
//
//    public String getLog049() {	
//        return log049;	
//    }
//
//    public String getLog050() {	
//        return log050;	
//    }
//
//    public String getLog051() {	
//        return log051;	
//    }
//
//    public String getLog052() {	
//        return log052;	
//    }
//
//    public String getLog053() {	
//        return log053;	
//    }
//
//    public String getLog054() {	
//        return log054;	
//    }
//
//    public String getLog055() {	
//        return log055;	
//    }
//
//    public String getLog056() {	
//        return log056;	
//    }
//
//    public String getLog057() {	
//        return log057;	
//    }
//
//    public String getLog058() {	
//        return log058;	
//    }
//
//    public String getLog059() {	
//        return log059;	
//    }
//
//    public String getLog060() {	
//        return log060;	
//    }
//
//    public String getLog061() {	
//        return log061;	
//    }
//
//    public String getLog062() {	
//        return log062;	
//    }
//
//    public String getLog063() {	
//        return log063;	
//    }
//
//    public String getLog064() {	
//        return log064;	
//    }
//
//    public String getLog065() {	
//        return log065;	
//    }
//
//    public String getLog066() {	
//        return log066;	
//    }
//
//    public String getLog067() {	
//        return log067;	
//    }
//
//    public String getLog068() {	
//        return log068;	
//    }
//
//    public String getLog069() {	
//        return log069;	
//    }
//
//    public String getLog070() {	
//        return log070;	
//    }
//
//    public String getLog071() {	
//        return log071;	
//    }
//
//    public String getLog072() {	
//        return log072;	
//    }
//
//    public String getLog073() {	
//        return log073;	
//    }
//
//    public String getLog074() {	
//        return log074;	
//    }
//
//    public String getLog075() {	
//        return log075;	
//    }
//
//    public String getLog076() {	
//        return log076;	
//    }
//
//    public String getLog077() {	
//        return log077;	
//    }
//
//    public String getLog078() {	
//        return log078;	
//    }
//
//    public String getLog079() {	
//        return log079;	
//    }
//
//    public String getLog080() {	
//        return log080;	
//    }
//
//    public String getLog081() {	
//        return log081;	
//    }
//
//    public String getLog082() {	
//        return log082;	
//    }
//
//    public String getLog083() {	
//        return log083;	
//    }
//
//    public String getLog084() {	
//        return log084;	
//    }
//
//    public String getLog085() {	
//        return log085;	
//    }
//
//    public String getLog086() {	
//        return log086;	
//    }
//
//    public String getLog087() {	
//        return log087;	
//    }
//
//    public String getLog088() {	
//        return log088;	
//    }
//
//    public String getLog089() {	
//        return log089;	
//    }
//
//    public String getLog090() {	
//        return log090;	
//    }
//
//    public String getLog091() {	
//        return log091;	
//    }
//
//    public String getLog092() {	
//        return log092;	
//    }
//
//    public String getLog093() {	
//        return log093;	
//    }
//
//    public String getLog094() {	
//        return log094;	
//    }
//
//    public String getLog095() {	
//        return log095;	
//    }
//
//    public String getLog096() {	
//        return log096;	
//    }
//
//    public String getLog097() {	
//        return log097;	
//    }
//
//    public String getLog098() {	
//        return log098;	
//    }
//
//    public String getLog099() {	
//        return log099;	
//    }
//
//    public String getLog100() {	
//        return log100;	
//    }
//
//    public String getLog101() {	
//        return log101;	
//    }
//
//    public String getLog102() {	
//        return log102;	
//    }
//
//    public String getLog103() {	
//        return log103;	
//    }
//
//    public String getLog104() {	
//        return log104;	
//    }
//
//    public String getLog105() {	
//        return log105;	
//    }
//
//    public String getLog106() {	
//        return log106;	
//    }
//
//    public String getLog107() {	
//        return log107;	
//    }
//
//    public String getLog108() {	
//        return log108;	
//    }
//
//    public String getLog109() {	
//        return log109;	
//    }
//
//    public String getLog110() {	
//        return log110;	
//    }
//
//    public String getLog111() {	
//        return log111;	
//    }
//
//    public String getLog112() {	
//        return log112;	
//    }
//
//    public String getLog113() {	
//        return log113;	
//    }
//
//    public String getLog114() {	
//        return log114;	
//    }
//
//    public String getLog115() {	
//        return log115;	
//    }
//
//    public String getLog116() {	
//        return log116;	
//    }
//
//    public String getLog117() {	
//        return log117;	
//    }
//
//    public String getLog118() {	
//        return log118;	
//    }
//
//    public String getLog119() {	
//        return log119;	
//    }
//
//    public String getLog120() {	
//        return log120;	
//    }
//
//    public String getLog121() {	
//        return log121;	
//    }
//
//    public String getLog122() {	
//        return log122;	
//    }
//
//    public String getLog123() {	
//        return log123;	
//    }
//
//    public String getLog124() {	
//        return log124;	
//    }
//
//    public String getLog125() {	
//        return log125;	
//    }
//
//    public String getLog126() {	
//        return log126;	
//    }
//
//    public String getLog127() {	
//        return log127;	
//    }
//
//    public String getLog128() {	
//        return log128;	
//    }
//
//    public String getLog129() {	
//        return log129;	
//    }
//
//    public String getLog130() {	
//        return log130;	
//    }
//
//    public String getLog131() {	
//        return log131;	
//    }
//
//    public String getLog132() {	
//        return log132;	
//    }
//
//    public String getLog133() {	
//        return log133;	
//    }
//
//    public String getLog134() {	
//        return log134;	
//    }
//
//    public String getLog135() {	
//        return log135;	
//    }
//
//    public String getLog136() {	
//        return log136;	
//    }
//
//    public String getLog137() {	
//        return log137;	
//    }
//
//    public String getLog138() {	
//        return log138;	
//    }
//
//    public String getLog139() {	
//        return log139;	
//    }
//
//    public String getLog140() {	
//        return log140;	
//    }
//
//    public String getLog141() {	
//        return log141;	
//    }
//
//    public String getLog142() {	
//        return log142;	
//    }
//
//    public String getLog143() {	
//        return log143;	
//    }
//
//    public String getLog144() {	
//        return log144;	
//    }
//
//    public String getLog145() {	
//        return log145;	
//    }
//
//    public String getLog146() {	
//        return log146;	
//    }
//
//    public String getLog147() {	
//        return log147;	
//    }
//
//    public String getLog148() {	
//        return log148;	
//    }
//
//    public String getLog149() {	
//        return log149;	
//    }
//
//    public String getLog150() {	
//        return log150;	
//    }
//
//    public String getLog151() {	
//        return log151;	
//    }
//
//    public String getLog152() {	
//        return log152;	
//    }
//
//    public String getLog153() {	
//        return log153;	
//    }
//
//    public String getLog154() {	
//        return log154;	
//    }
//
//    public String getLog155() {	
//        return log155;	
//    }
//
//    public String getLog156() {	
//        return log156;	
//    }
//
//    public String getLog157() {	
//        return log157;	
//    }
//
//    public String getLog158() {	
//        return log158;	
//    }
//
//    public String getLog159() {	
//        return log159;	
//    }
//
//    public String getLog160() {	
//        return log160;	
//    }
//
//    public String getLog161() {	
//        return log161;	
//    }
//
//    public String getLog162() {	
//        return log162;	
//    }
//
//    public String getLog163() {	
//        return log163;	
//    }
//
//    public String getLog164() {	
//        return log164;	
//    }
//
//    public String getLog165() {	
//        return log165;	
//    }
//
//    public String getLog166() {	
//        return log166;	
//    }
//
//    public String getLog167() {	
//        return log167;	
//    }
//
//    public String getLog168() {	
//        return log168;	
//    }
//
//    public String getLog169() {	
//        return log169;	
//    }
//
//    public String getLog170() {	
//        return log170;	
//    }
//
//    public String getLog171() {	
//        return log171;	
//    }
//
//    public String getLog172() {	
//        return log172;	
//    }
//
//    public String getLog173() {	
//        return log173;	
//    }
//
//    public String getLog174() {	
//        return log174;	
//    }
//
//    public String getLog175() {	
//        return log175;	
//    }
//
//    public String getLog176() {	
//        return log176;	
//    }
//
//    public String getLog177() {	
//        return log177;	
//    }
//
//    public String getLog178() {	
//        return log178;	
//    }
//
//    public String getLog179() {	
//        return log179;	
//    }
//
//    public String getLog180() {	
//        return log180;	
//    }
//
//    public String getLog181() {	
//        return log181;	
//    }
//
//    public String getLog182() {	
//        return log182;	
//    }
//
//    public String getLog183() {	
//        return log183;	
//    }
//
//    public String getLog184() {	
//        return log184;	
//    }
//
//    public String getLog185() {	
//        return log185;	
//    }
//
//    public String getLog186() {	
//        return log186;	
//    }
//
//    public String getLog187() {	
//        return log187;	
//    }
//
//    public String getLog188() {	
//        return log188;	
//    }
//
//    public String getLog189() {	
//        return log189;	
//    }
//
//    public String getLog190() {	
//        return log190;	
//    }
//
//    public String getLog191() {	
//        return log191;	
//    }
//
//    public String getLog192() {	
//        return log192;	
//    }

    public String getPower() {
        return power;
    }

    public String getAutoMode() {
        return autoMode;
    }

    public String getCycleStart() {
        return cycleStart;
    }

    public String getAlert() {
        return alert;
    }

    public String getAlertInfo() {
        return alertInfo;
    }

    public String getShotCnt() {
        return shotCnt;
    }

    public String getCycleTime() {
        return cycleTime;
    }

    public String getWeighingTime() {
        return weighingTime;
    }

    public String getMinCushionPos() {
        return minCushionPos;
    }

    public String getFillingPeak() {
        return fillingPeak;
    }

    public String getMaxPressure() {
        return maxPressure;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public void setEventNo(String eventNo) {
        this.eventNo = eventNo;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public void setInfo01(String info01) {
        this.info01 = info01;
    }

    public void setInfo02(String info02) {
        this.info02 = info02;
    }

    public void setInfo03(String info03) {
        this.info03 = info03;
    }

    public void setInfo04(String info04) {
        this.info04 = info04;
    }

    public void setInfo05(String info05) {
        this.info05 = info05;
    }

    public void setInfo06(String info06) {
        this.info06 = info06;
    }

    public void setInfo07(String info07) {
        this.info07 = info07;
    }

    public void setInfo08(String info08) {
        this.info08 = info08;
    }

    public void setInfo09(String info09) {
        this.info09 = info09;
    }

    public void setInfo10(String info10) {
        this.info10 = info10;
    }

    public void setInfo11(String info11) {
        this.info11 = info11;
    }

    public void setInfo12(String info12) {
        this.info12 = info12;
    }

    public void setInfo13(String info13) {
        this.info13 = info13;
    }

    public void setInfo14(String info14) {
        this.info14 = info14;
    }

    public void setInfo15(String info15) {
        this.info15 = info15;
    }

    public void setInfo16(String info16) {
        this.info16 = info16;
    }

    public void setInfo17(String info17) {
        this.info17 = info17;
    }

    public void setInfo18(String info18) {
        this.info18 = info18;
    }

    public void setInfo19(String info19) {
        this.info19 = info19;
    }

    public void setInfo20(String info20) {
        this.info20 = info20;
    }

    public void setInfo21(String info21) {
        this.info21 = info21;
    }

    public void setInfo22(String info22) {
        this.info22 = info22;
    }

    public void setInfo23(String info23) {
        this.info23 = info23;
    }

    public void setInfo24(String info24) {
        this.info24 = info24;
    }

    public void setInfo25(String info25) {
        this.info25 = info25;
    }

    public void setInfo26(String info26) {
        this.info26 = info26;
    }

    public void setInfo27(String info27) {
        this.info27 = info27;
    }

    public void setInfo28(String info28) {
        this.info28 = info28;
    }

    public void setInfo29(String info29) {
        this.info29 = info29;
    }

    public void setInfo30(String info30) {
        this.info30 = info30;
    }

    public void setInfo31(String info31) {
        this.info31 = info31;
    }

    public void setInfo32(String info32) {
        this.info32 = info32;
    }

    public void setCol01(String col01) {
        this.col01 = col01;
    }

    public void setCol02(String col02) {
        this.col02 = col02;
    }

    public void setCol03(String col03) {
        this.col03 = col03;
    }

    public void setCol04(String col04) {
        this.col04 = col04;
    }

    public void setCol05(String col05) {
        this.col05 = col05;
    }

    public void setCol06(String col06) {
        this.col06 = col06;
    }

    public void setCol07(String col07) {
        this.col07 = col07;
    }

    public void setCol08(String col08) {
        this.col08 = col08;
    }

    public void setCol09(String col09) {
        this.col09 = col09;
    }

    public void setCol10(String col10) {
        this.col10 = col10;
    }

    public void setCol11(String col11) {
        this.col11 = col11;
    }

    public void setCol12(String col12) {
        this.col12 = col12;
    }

    public void setCol13(String col13) {
        this.col13 = col13;
    }

    public void setCol14(String col14) {
        this.col14 = col14;
    }

    public void setCol15(String col15) {
        this.col15 = col15;
    }

    public void setCol16(String col16) {
        this.col16 = col16;
    }

    public void setCol17(String col17) {
        this.col17 = col17;
    }

    public void setCol18(String col18) {
        this.col18 = col18;
    }

    public void setCol19(String col19) {
        this.col19 = col19;
    }

    public void setCol20(String col20) {
        this.col20 = col20;
    }

    public void setCol21(String col21) {
        this.col21 = col21;
    }

    public void setCol22(String col22) {
        this.col22 = col22;
    }

    public void setCol23(String col23) {
        this.col23 = col23;
    }

    public void setCol24(String col24) {
        this.col24 = col24;
    }

    public void setCol25(String col25) {
        this.col25 = col25;
    }

    public void setCol26(String col26) {
        this.col26 = col26;
    }

    public void setCol27(String col27) {
        this.col27 = col27;
    }

    public void setCol28(String col28) {
        this.col28 = col28;
    }

    public void setCol29(String col29) {
        this.col29 = col29;
    }

    public void setCol30(String col30) {
        this.col30 = col30;
    }

    public void setCol31(String col31) {
        this.col31 = col31;
    }

    public void setCol32(String col32) {
        this.col32 = col32;
    }
    
//    public void setLog001(String log001) {	
//	this.log001 = log001;
//    }
//
//    public void setLog002(String log002) {	
//	this.log002 = log002;
//    }
//
//    public void setLog003(String log003) {	
//	this.log003 = log003;
//    }
//
//    public void setLog004(String log004) {	
//	this.log004 = log004;
//    }
//
//    public void setLog005(String log005) {	
//	this.log005 = log005;
//    }
//
//    public void setLog006(String log006) {	
//	this.log006 = log006;
//    }
//
//    public void setLog007(String log007) {	
//	this.log007 = log007;
//    }
//
//    public void setLog008(String log008) {	
//	this.log008 = log008;
//    }
//
//    public void setLog009(String log009) {	
//	this.log009 = log009;
//    }
//
//    public void setLog010(String log010) {	
//	this.log010 = log010;
//    }
//
//    public void setLog011(String log011) {	
//	this.log011 = log011;
//    }
//
//    public void setLog012(String log012) {	
//	this.log012 = log012;
//    }
//
//    public void setLog013(String log013) {	
//	this.log013 = log013;
//    }
//
//    public void setLog014(String log014) {	
//	this.log014 = log014;
//    }
//
//    public void setLog015(String log015) {	
//	this.log015 = log015;
//    }
//
//    public void setLog016(String log016) {	
//	this.log016 = log016;
//    }
//
//    public void setLog017(String log017) {	
//	this.log017 = log017;
//    }
//
//    public void setLog018(String log018) {	
//	this.log018 = log018;
//    }
//
//    public void setLog019(String log019) {	
//	this.log019 = log019;
//    }
//
//    public void setLog020(String log020) {	
//	this.log020 = log020;
//    }
//
//    public void setLog021(String log021) {	
//	this.log021 = log021;
//    }
//
//    public void setLog022(String log022) {	
//	this.log022 = log022;
//    }
//
//    public void setLog023(String log023) {	
//	this.log023 = log023;
//    }
//
//    public void setLog024(String log024) {	
//	this.log024 = log024;
//    }
//
//    public void setLog025(String log025) {	
//	this.log025 = log025;
//    }
//
//    public void setLog026(String log026) {	
//	this.log026 = log026;
//    }
//
//    public void setLog027(String log027) {	
//	this.log027 = log027;
//    }
//
//    public void setLog028(String log028) {	
//	this.log028 = log028;
//    }
//
//    public void setLog029(String log029) {	
//	this.log029 = log029;
//    }
//
//    public void setLog030(String log030) {	
//	this.log030 = log030;
//    }
//
//    public void setLog031(String log031) {	
//	this.log031 = log031;
//    }
//
//    public void setLog032(String log032) {	
//	this.log032 = log032;
//    }
//
//    public void setLog033(String log033) {	
//	this.log033 = log033;
//    }
//
//    public void setLog034(String log034) {	
//	this.log034 = log034;
//    }
//
//    public void setLog035(String log035) {	
//	this.log035 = log035;
//    }
//
//    public void setLog036(String log036) {	
//	this.log036 = log036;
//    }
//
//    public void setLog037(String log037) {	
//	this.log037 = log037;
//    }
//
//    public void setLog038(String log038) {	
//	this.log038 = log038;
//    }
//
//    public void setLog039(String log039) {	
//	this.log039 = log039;
//    }
//
//    public void setLog040(String log040) {	
//	this.log040 = log040;
//    }
//
//    public void setLog041(String log041) {	
//	this.log041 = log041;
//    }
//
//    public void setLog042(String log042) {	
//	this.log042 = log042;
//    }
//
//    public void setLog043(String log043) {	
//	this.log043 = log043;
//    }
//
//    public void setLog044(String log044) {	
//	this.log044 = log044;
//    }
//
//    public void setLog045(String log045) {	
//	this.log045 = log045;
//    }
//
//    public void setLog046(String log046) {	
//	this.log046 = log046;
//    }
//
//    public void setLog047(String log047) {	
//	this.log047 = log047;
//    }
//
//    public void setLog048(String log048) {	
//	this.log048 = log048;
//    }
//
//    public void setLog049(String log049) {	
//	this.log049 = log049;
//    }
//
//    public void setLog050(String log050) {	
//	this.log050 = log050;
//    }
//
//    public void setLog051(String log051) {	
//	this.log051 = log051;
//    }
//
//    public void setLog052(String log052) {	
//	this.log052 = log052;
//    }
//
//    public void setLog053(String log053) {	
//	this.log053 = log053;
//    }
//
//    public void setLog054(String log054) {	
//	this.log054 = log054;
//    }
//
//    public void setLog055(String log055) {	
//	this.log055 = log055;
//    }
//
//    public void setLog056(String log056) {	
//	this.log056 = log056;
//    }
//
//    public void setLog057(String log057) {	
//	this.log057 = log057;
//    }
//
//    public void setLog058(String log058) {	
//	this.log058 = log058;
//    }
//
//    public void setLog059(String log059) {	
//	this.log059 = log059;
//    }
//
//    public void setLog060(String log060) {	
//	this.log060 = log060;
//    }
//
//    public void setLog061(String log061) {	
//	this.log061 = log061;
//    }
//
//    public void setLog062(String log062) {	
//	this.log062 = log062;
//    }
//
//    public void setLog063(String log063) {	
//	this.log063 = log063;
//    }
//
//    public void setLog064(String log064) {	
//	this.log064 = log064;
//    }
//
//    public void setLog065(String log065) {	
//	this.log065 = log065;
//    }
//
//    public void setLog066(String log066) {	
//	this.log066 = log066;
//    }
//
//    public void setLog067(String log067) {	
//	this.log067 = log067;
//    }
//
//    public void setLog068(String log068) {	
//	this.log068 = log068;
//    }
//
//    public void setLog069(String log069) {	
//	this.log069 = log069;
//    }
//
//    public void setLog070(String log070) {	
//	this.log070 = log070;
//    }
//
//    public void setLog071(String log071) {	
//	this.log071 = log071;
//    }
//
//    public void setLog072(String log072) {	
//	this.log072 = log072;
//    }
//
//    public void setLog073(String log073) {	
//	this.log073 = log073;
//    }
//
//    public void setLog074(String log074) {	
//	this.log074 = log074;
//    }
//
//    public void setLog075(String log075) {	
//	this.log075 = log075;
//    }
//
//    public void setLog076(String log076) {	
//	this.log076 = log076;
//    }
//
//    public void setLog077(String log077) {	
//	this.log077 = log077;
//    }
//
//    public void setLog078(String log078) {	
//	this.log078 = log078;
//    }
//
//    public void setLog079(String log079) {	
//	this.log079 = log079;
//    }
//
//    public void setLog080(String log080) {	
//	this.log080 = log080;
//    }
//
//    public void setLog081(String log081) {	
//	this.log081 = log081;
//    }
//
//    public void setLog082(String log082) {	
//	this.log082 = log082;
//    }
//
//    public void setLog083(String log083) {	
//	this.log083 = log083;
//    }
//
//    public void setLog084(String log084) {	
//	this.log084 = log084;
//    }
//
//    public void setLog085(String log085) {	
//	this.log085 = log085;
//    }
//
//    public void setLog086(String log086) {	
//	this.log086 = log086;
//    }
//
//    public void setLog087(String log087) {	
//	this.log087 = log087;
//    }
//
//    public void setLog088(String log088) {	
//	this.log088 = log088;
//    }
//
//    public void setLog089(String log089) {	
//	this.log089 = log089;
//    }
//
//    public void setLog090(String log090) {	
//	this.log090 = log090;
//    }
//
//    public void setLog091(String log091) {	
//	this.log091 = log091;
//    }
//
//    public void setLog092(String log092) {	
//	this.log092 = log092;
//    }
//
//    public void setLog093(String log093) {	
//	this.log093 = log093;
//    }
//
//    public void setLog094(String log094) {	
//	this.log094 = log094;
//    }
//
//    public void setLog095(String log095) {	
//	this.log095 = log095;
//    }
//
//    public void setLog096(String log096) {	
//	this.log096 = log096;
//    }
//
//    public void setLog097(String log097) {	
//	this.log097 = log097;
//    }
//
//    public void setLog098(String log098) {	
//	this.log098 = log098;
//    }
//
//    public void setLog099(String log099) {	
//	this.log099 = log099;
//    }
//
//    public void setLog100(String log100) {	
//	this.log100 = log100;
//    }
//
//    public void setLog101(String log101) {	
//	this.log101 = log101;
//    }
//
//    public void setLog102(String log102) {	
//	this.log102 = log102;
//    }
//
//    public void setLog103(String log103) {	
//	this.log103 = log103;
//    }
//
//    public void setLog104(String log104) {	
//	this.log104 = log104;
//    }
//
//    public void setLog105(String log105) {	
//	this.log105 = log105;
//    }
//
//    public void setLog106(String log106) {	
//	this.log106 = log106;
//    }
//
//    public void setLog107(String log107) {	
//	this.log107 = log107;
//    }
//
//    public void setLog108(String log108) {	
//	this.log108 = log108;
//    }
//
//    public void setLog109(String log109) {	
//	this.log109 = log109;
//    }
//
//    public void setLog110(String log110) {	
//	this.log110 = log110;
//    }
//
//    public void setLog111(String log111) {	
//	this.log111 = log111;
//    }
//
//    public void setLog112(String log112) {	
//	this.log112 = log112;
//    }
//
//    public void setLog113(String log113) {	
//	this.log113 = log113;
//    }
//
//    public void setLog114(String log114) {	
//	this.log114 = log114;
//    }
//
//    public void setLog115(String log115) {	
//	this.log115 = log115;
//    }
//
//    public void setLog116(String log116) {	
//	this.log116 = log116;
//    }
//
//    public void setLog117(String log117) {	
//	this.log117 = log117;
//    }
//
//    public void setLog118(String log118) {	
//	this.log118 = log118;
//    }
//
//    public void setLog119(String log119) {	
//	this.log119 = log119;
//    }
//
//    public void setLog120(String log120) {	
//	this.log120 = log120;
//    }
//
//    public void setLog121(String log121) {	
//	this.log121 = log121;
//    }
//
//    public void setLog122(String log122) {	
//	this.log122 = log122;
//    }
//
//    public void setLog123(String log123) {	
//	this.log123 = log123;
//    }
//
//    public void setLog124(String log124) {	
//	this.log124 = log124;
//    }
//
//    public void setLog125(String log125) {	
//	this.log125 = log125;
//    }
//
//    public void setLog126(String log126) {	
//	this.log126 = log126;
//    }
//
//    public void setLog127(String log127) {	
//	this.log127 = log127;
//    }
//
//    public void setLog128(String log128) {	
//	this.log128 = log128;
//    }
//
//    public void setLog129(String log129) {	
//	this.log129 = log129;
//    }
//
//    public void setLog130(String log130) {	
//	this.log130 = log130;
//    }
//
//    public void setLog131(String log131) {	
//	this.log131 = log131;
//    }
//
//    public void setLog132(String log132) {	
//	this.log132 = log132;
//    }
//
//    public void setLog133(String log133) {	
//	this.log133 = log133;
//    }
//
//    public void setLog134(String log134) {	
//	this.log134 = log134;
//    }
//
//    public void setLog135(String log135) {	
//	this.log135 = log135;
//    }
//
//    public void setLog136(String log136) {	
//	this.log136 = log136;
//    }
//
//    public void setLog137(String log137) {	
//	this.log137 = log137;
//    }
//
//    public void setLog138(String log138) {	
//	this.log138 = log138;
//    }
//
//    public void setLog139(String log139) {	
//	this.log139 = log139;
//    }
//
//    public void setLog140(String log140) {	
//	this.log140 = log140;
//    }
//
//    public void setLog141(String log141) {	
//	this.log141 = log141;
//    }
//
//    public void setLog142(String log142) {	
//	this.log142 = log142;
//    }
//
//    public void setLog143(String log143) {	
//	this.log143 = log143;
//    }
//
//    public void setLog144(String log144) {	
//	this.log144 = log144;
//    }
//
//    public void setLog145(String log145) {	
//	this.log145 = log145;
//    }
//
//    public void setLog146(String log146) {	
//	this.log146 = log146;
//    }
//
//    public void setLog147(String log147) {	
//	this.log147 = log147;
//    }
//
//    public void setLog148(String log148) {	
//	this.log148 = log148;
//    }
//
//    public void setLog149(String log149) {	
//	this.log149 = log149;
//    }
//
//    public void setLog150(String log150) {	
//	this.log150 = log150;
//    }
//
//    public void setLog151(String log151) {	
//	this.log151 = log151;
//    }
//
//    public void setLog152(String log152) {	
//	this.log152 = log152;
//    }
//
//    public void setLog153(String log153) {	
//	this.log153 = log153;
//    }
//
//    public void setLog154(String log154) {	
//	this.log154 = log154;
//    }
//
//    public void setLog155(String log155) {	
//	this.log155 = log155;
//    }
//
//    public void setLog156(String log156) {	
//	this.log156 = log156;
//    }
//
//    public void setLog157(String log157) {	
//	this.log157 = log157;
//    }
//
//    public void setLog158(String log158) {	
//	this.log158 = log158;
//    }
//
//    public void setLog159(String log159) {	
//	this.log159 = log159;
//    }
//
//    public void setLog160(String log160) {	
//	this.log160 = log160;
//    }
//
//    public void setLog161(String log161) {	
//	this.log161 = log161;
//    }
//
//    public void setLog162(String log162) {	
//	this.log162 = log162;
//    }
//
//    public void setLog163(String log163) {	
//	this.log163 = log163;
//    }
//
//    public void setLog164(String log164) {	
//	this.log164 = log164;
//    }
//
//    public void setLog165(String log165) {	
//	this.log165 = log165;
//    }
//
//    public void setLog166(String log166) {	
//	this.log166 = log166;
//    }
//
//    public void setLog167(String log167) {	
//	this.log167 = log167;
//    }
//
//    public void setLog168(String log168) {	
//	this.log168 = log168;
//    }
//
//    public void setLog169(String log169) {	
//	this.log169 = log169;
//    }
//
//    public void setLog170(String log170) {	
//	this.log170 = log170;
//    }
//
//    public void setLog171(String log171) {	
//	this.log171 = log171;
//    }
//
//    public void setLog172(String log172) {	
//	this.log172 = log172;
//    }
//
//    public void setLog173(String log173) {	
//	this.log173 = log173;
//    }
//
//    public void setLog174(String log174) {	
//	this.log174 = log174;
//    }
//
//    public void setLog175(String log175) {	
//	this.log175 = log175;
//    }
//
//    public void setLog176(String log176) {	
//	this.log176 = log176;
//    }
//
//    public void setLog177(String log177) {	
//	this.log177 = log177;
//    }
//
//    public void setLog178(String log178) {	
//	this.log178 = log178;
//    }
//
//    public void setLog179(String log179) {	
//	this.log179 = log179;
//    }
//
//    public void setLog180(String log180) {	
//	this.log180 = log180;
//    }
//
//    public void setLog181(String log181) {	
//	this.log181 = log181;
//    }
//
//    public void setLog182(String log182) {	
//	this.log182 = log182;
//    }
//
//    public void setLog183(String log183) {	
//	this.log183 = log183;
//    }
//
//    public void setLog184(String log184) {	
//	this.log184 = log184;
//    }
//
//    public void setLog185(String log185) {	
//	this.log185 = log185;
//    }
//
//    public void setLog186(String log186) {	
//	this.log186 = log186;
//    }
//
//    public void setLog187(String log187) {	
//	this.log187 = log187;
//    }
//
//    public void setLog188(String log188) {	
//	this.log188 = log188;
//    }
//
//    public void setLog189(String log189) {	
//	this.log189 = log189;
//    }
//
//    public void setLog190(String log190) {	
//	this.log190 = log190;
//    }
//
//    public void setLog191(String log191) {	
//	this.log191 = log191;
//    }
//
//    public void setLog192(String log192) {	
//	this.log192 = log192;
//    }    

    public void setPower(String power) {
        this.power = power;
    }

    public void setAutoMode(String autoMode) {
        this.autoMode = autoMode;
    }

    public void setCycleStart(String cycleStart) {
        this.cycleStart = cycleStart;
    }

    public void setAlert(String alert) {
        this.alert = alert;
    }

    public void setAlertInfo(String alertInfo) {
        this.alertInfo = alertInfo;
    }

    public void setShotCnt(String shotCnt) {
        this.shotCnt = shotCnt;
    }

    public void setCycleTime(String cycleTime) {
        this.cycleTime = cycleTime;
    }

    public void setWeighingTime(String weighingTime) {
        this.weighingTime = weighingTime;
    }

    public void setMinCushionPos(String minCushionPos) {
        this.minCushionPos = minCushionPos;
    }

    public void setFillingPeak(String fillingPeak) {
        this.fillingPeak = fillingPeak;
    }

    public void setMaxPressure(String maxPressure) {
        this.maxPressure = maxPressure;
    }

    public String getMachineUuid() {
        return machineUuid;
    }

    public void setMachineUuid(String machineUuid) {
        this.machineUuid = machineUuid;
    }

    public TblSigmaLog getTblSigmaLog() {
        return tblSigmaLog;
    }

    public void setTblSigmaLog(TblSigmaLog tblSigmaLog) {
        this.tblSigmaLog = tblSigmaLog;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

}
