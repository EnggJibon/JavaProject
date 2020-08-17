/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.mold.maintenance.remodeling;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.CountResponse;
import com.kmcj.karte.FileReponse;
import com.kmcj.karte.UrlDecodeInterceptor;
import com.kmcj.karte.conf.CnfSystem;
import com.kmcj.karte.conf.CnfSystemService;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.issue.TblIssueVo;
import com.kmcj.karte.resources.mold.part.rel.MstMoldPartDetailMaintenance;
import com.kmcj.karte.resources.mold.part.rel.MstMoldPartRelService;
import com.kmcj.karte.util.FileUtil;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.interceptor.Interceptors;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author admin
 */
@RequestScoped
@Path("mold/maintenance/remodeling")
public class TblMoldMaintenanceRemodelingResource {

    public TblMoldMaintenanceRemodelingResource() {

    }

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;
    
    @Inject
    private TblMoldMaintenanceRemodelingService tblMoldMaintenanceRemodelingService;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private CnfSystemService cnfSystemService;
    
    @Inject
    private MstMoldPartRelService mstMoldPartRelService;

    /**
     * T0007	金型メンテナンス照会 金型メンテナンス改造件数取得
     *
     * @param mainteDateStart //メンテナンス日start
     * @param mainteDateEnd //メンテナンス日end
     * @param mainteOrRemodel //改造・メンテナンス区分
     * @param moldId //金型ID
     * @param moldName //金型名称
     * @param mainteStatus
     * @param department
     * @return
     */
    @GET
    @Path("count")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public CountResponse getRecordCount(@QueryParam("mainteDateStart") String mainteDateStart,
            @QueryParam("mainteDateEnd") String mainteDateEnd,
            @QueryParam("mainteOrRemodel") String mainteOrRemodel,
            @QueryParam("moldId") String moldId,
            @QueryParam("moldName") String moldName,
            @QueryParam("mainteStatus") String mainteStatus,
            @QueryParam("department") String department) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblMoldMaintenanceRemodelingVo queryVo = new TblMoldMaintenanceRemodelingVo();
        FileUtil fu = new FileUtil();
        queryVo.setMainteDateStart(null == mainteDateStart ? null : fu.getDateParseForDate(mainteDateStart));
        queryVo.setMainteDateEnd(null == mainteDateEnd ? null : fu.getDateParseForDate(mainteDateEnd));
        queryVo.setMainteOrRemodel(null == mainteOrRemodel ? null : Integer.parseInt(mainteOrRemodel));
        MstMold mstMold = new MstMold();
        mstMold.setDepartment(null == department ? null : Integer.parseInt(department));
        queryVo.setMstMold(mstMold);
        queryVo.setMoldId(moldId);
        queryVo.setMoldName(moldName);
        queryVo.setMainteStatus(mainteStatus);
        CountResponse count = (CountResponse) tblMoldMaintenanceRemodelingService.getRecordCount(queryVo, loginUser);
        CnfSystem cnf = cnfSystemService.findByKey("system", "max_list_record_count");
        long sysCount = Long.parseLong(cnf.getConfigValue());
        if (count.getCount() > sysCount) {
            count.setError(true);
            count.setErrorCode(ErrorMessages.E201_APPLICATION);
            String msg = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_confirm_max_record_count");
            msg = String.format(msg, sysCount);
            count.setErrorMessage(msg);
        }

        return count;
    }

    /**
     * T0007	金型メンテナンス改造情報を取得
     *
     * @param mainteDateStart //メンテナンス日start
     * @param mainteDateEnd //メンテナンス日end
     * @param mainteOrRemodel //改造・メンテナンス区分
     * @param moldId //金型ID
     * @param moldName //金型名称
     * @param mainteStatus
     * @param department
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldMaintenanceRemodelingVo getMaintenanceRemodeling(@QueryParam("mainteDateStart") String mainteDateStart,
            @QueryParam("mainteDateEnd") String mainteDateEnd,
            @QueryParam("mainteOrRemodel") String mainteOrRemodel,
            @QueryParam("moldId") String moldId,
            @QueryParam("moldName") String moldName,
            @QueryParam("mainteStatus") String mainteStatus,
            @QueryParam("department") String department,
            @QueryParam("orderKey") String startDatetime // 1:金型メンテナンス開始日時の昇順
    ) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblMoldMaintenanceRemodelingVo queryVo = new TblMoldMaintenanceRemodelingVo();
        FileUtil fu = new FileUtil();
        queryVo.setMainteDateStart(null == mainteDateStart ? null : fu.getDateParseForDate(mainteDateStart));
        queryVo.setMainteDateEnd(null == mainteDateEnd ? null : fu.getDateParseForDate(mainteDateEnd));
        queryVo.setMainteOrRemodel(null == mainteOrRemodel ? null : Integer.parseInt(mainteOrRemodel));
        MstMold mstMold = new MstMold();
        mstMold.setDepartment(null == department ? null : Integer.parseInt(department));
        queryVo.setMstMold(mstMold);
        queryVo.setMoldId(moldId);
        queryVo.setMoldName(moldName);
        queryVo.setMainteStatus(mainteStatus);
        queryVo.setStartDatetime(startDatetime);
        BasicResponse br = tblMoldMaintenanceRemodelingService.getMaintenanceRemodeling(queryVo, loginUser);
        return (TblMoldMaintenanceRemodelingVo) br;
    }
    /**
     * 会社マスタ取得(getMoldPartRel)
     *
     * @param id
     * @return
     */
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    @Interceptors({UrlDecodeInterceptor.class})
    public List<MstMoldPartDetailMaintenance> getMstMoldPartRelDetail(@PathParam("id") String id) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstMoldPartRelService.getMstMoldPartRelDetailByMaintId(id, loginUser);
    }
    /**
     * 金型メンテナンス開始を宣言するため
     *
     * @param issueVo
     * @return
     */
    @POST
    @Path("start")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMoldMaintenanceStart(TblIssueVo issueVo) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return tblMoldMaintenanceRemodelingService.postMoldMaintenanceStart(issueVo, loginUser);
    }


    /**
     * 金型メンテナンス開始2
     *
     * @param issueVo
     * @return
     */
    @POST
    @Path("startmainte")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldMaintenanceRemodelingVo postMoldMaintenanceStart2(TblMoldMaintenanceRemodelingVo tblMoldMaintenanceRemodelingVo) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        return tblMoldMaintenanceRemodelingService.postMoldMaintenanceStart2(tblMoldMaintenanceRemodelingVo, loginUser);
    }

    /**
     * TT0008 金型改造開始入力 金型改造開始 金型改造を宣言するため、データベースを更新する。
     *
     * @param tblMoldMaintenanceRemodelingVo
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldMaintenanceRemodelingVo postRemodeling(TblMoldMaintenanceRemodelingVo tblMoldMaintenanceRemodelingVo) {
        TblMoldMaintenanceRemodelingVo result = new TblMoldMaintenanceRemodelingVo();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        String moldId = tblMoldMaintenanceRemodelingVo.getMoldId();
        
        //外部データチェック
        BasicResponse response = FileUtil.checkExternal(entityManager, mstDictionaryService, moldId, loginUser);
        if (response.isError()) {
            result.setError(response.isError());
            result.setErrorCode(response.getErrorCode());
            result.setErrorMessage(response.getErrorMessage());
            return result;
        }
        
        //メンテイ状態チェック
        int mainteStatus = tblMoldMaintenanceRemodelingService.getMoldMainteStatus(moldId);
        if (mainteStatus == 1) {
            result.setError(true);
            result.setErrorCode(ErrorMessages.MSG201_APPLICATION);
            result.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_mold_under_maintenance"));
        } //メンテイ状態チェック
        else if (mainteStatus == 2) {
            result.setError(true);
            result.setErrorCode(ErrorMessages.MSG201_APPLICATION);
            result.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_mold_remodeling"));
        } else {
            result = tblMoldMaintenanceRemodelingService.changeMstMoldMainteStatus(tblMoldMaintenanceRemodelingVo, loginUser);
        }
        return result;
    }

    /**
     * T0007	金型メンテナンス照会	金型メンテナンス削除
     *
     * @param id
     * @return
     */
    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse deleteMaintenanceRemodeling(@QueryParam("id") String id) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMoldMaintenanceRemodelingService.deleteMaintenanceRemodeling(id, loginUser);
    }
    
    /**
     * 金型メンテナンス照会CSV出力
     *
     * @param mainteDateStart //メンテナンス日start
     * @param mainteDateEnd //メンテナンス日end
     * @param mainteOrRemodel //改造・メンテナンス区分
     * @param moldId //金型ID
     * @param moldName //金型名称
     * @param mainteStatus
     * @return
     */
    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getMoldsCSV(@QueryParam("mainteDateStart") String mainteDateStart,
            @QueryParam("mainteDateEnd") String mainteDateEnd,
            @QueryParam("mainteOrRemodel") String mainteOrRemodel,
            @QueryParam("moldId") String moldId,
            @QueryParam("moldName") String moldName,
            @QueryParam("mainteStatus") String mainteStatus,
            @QueryParam("department") String department) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblMoldMaintenanceRemodelingVo queryVo = new TblMoldMaintenanceRemodelingVo();
        FileUtil fu = new FileUtil();
        queryVo.setMainteDateStart("".equals(mainteDateStart) ? null : fu.getDateParseForDate(mainteDateStart));
        queryVo.setMainteDateEnd("".equals(mainteDateEnd) ? null : fu.getDateParseForDate(mainteDateEnd));
        queryVo.setMainteOrRemodel("".equals(mainteOrRemodel) ? null : Integer.parseInt(mainteOrRemodel));
        MstMold mstMold = new MstMold();
        mstMold.setDepartment(null == department ? null : Integer.parseInt(department));
        queryVo.setMstMold(mstMold);
        queryVo.setMoldId(moldId);
        queryVo.setMoldName(moldName);
        queryVo.setMainteStatus(mainteStatus);

        FileReponse fr = tblMoldMaintenanceRemodelingService.getTblMoldMaintenanceRemodelingOutputCsv(queryVo, loginUser);
        return fr;
    }


    /**
     * T0008	金型メンテナンス詳細 金型メンテナンス改造詳細情報を取得
     *
     * @param id
     * @return
     */
    @GET
    @Path("detail/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldMaintenanceRemodelingVo getMaintenanceDetail(@PathParam("id") String id) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return tblMoldMaintenanceRemodelingService.getMoldmainteOrRemodelDetails(id, null, CommonConstants.MAINTEORREMODEL_MAINTE, loginUser);
    }
    
    /**
     * バッチで金型メンテナンス改造テーブルデータを取得
     *
     * @param latestExecutedDate
     * @param moldUuid
     * @return
     */
    @GET
    @Path("extmoldmaintenanceremodeling")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldMaintenanceRemodelingList getExtMoldMaintenanceRemodelingsByBatch(@QueryParam("latestExecutedDate") String latestExecutedDate, @QueryParam("moldUuid") String moldUuid) {
        return tblMoldMaintenanceRemodelingService.getExtMoldMaintenanceRemodelingsByBatch(latestExecutedDate, moldUuid);
    }
    
    /**
     * T0007    金型メンテナンス改造情報を取得
     *
     * @param mainteDateStart //メンテナンス日start
     * @param mainteDateEnd //メンテナンス日end
     * @param mainteOrRemodel //改造・メンテナンス区分
     * @param moldId //金型ID
     * @param moldName //金型名称
     * @param mainteStatus
     * @param department
     * @param sidx
     * @param sord
     * @param pageNumber
     * @param pageSize
     * @return
     */
    @GET
    @Path("search")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TblMoldMaintenanceRemodelingVo getMaintenanceRemodelingByPage(
            @QueryParam("mainteDateStart") String mainteDateStart, @QueryParam("mainteDateEnd") String mainteDateEnd,
            @QueryParam("mainteOrRemodel") String mainteOrRemodel, @QueryParam("moldId") String moldId,
            @QueryParam("moldName") String moldName, @QueryParam("mainteStatus") String mainteStatus,
            @QueryParam("department") String department, @QueryParam("sidx") String sidx // ソートキー
            , @QueryParam("sord") String sord // ソート順
            , @QueryParam("pageNumber") int pageNumber // ページNo
            , @QueryParam("pageSize") int pageSize // ページSize
            , @QueryParam("moldUuid") String moldUuid
    ) {

        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        TblMoldMaintenanceRemodelingVo queryVo = new TblMoldMaintenanceRemodelingVo();
        FileUtil fu = new FileUtil();
        queryVo.setMainteDateStart(null == mainteDateStart ? null : fu.getDateParseForDate(mainteDateStart));
        queryVo.setMainteDateEnd(null == mainteDateEnd ? null : fu.getDateParseForDate(mainteDateEnd));
        queryVo.setMainteOrRemodel(null == mainteOrRemodel ? null : Integer.parseInt(mainteOrRemodel));
        MstMold mstMold = new MstMold();
        mstMold.setDepartment(null == department ? null : Integer.parseInt(department));
        queryVo.setMstMold(mstMold);
        queryVo.setMoldId(moldId);
        queryVo.setMoldName(moldName);
        queryVo.setMainteStatus(mainteStatus);
        queryVo.setMoldUuid(moldUuid);
        BasicResponse br = tblMoldMaintenanceRemodelingService.getMaintenanceRemodelingByPage(queryVo, loginUser, sidx,
                sord, pageNumber, pageSize, true);
        return (TblMoldMaintenanceRemodelingVo) br;
    }

}
