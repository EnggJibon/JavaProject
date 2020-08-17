package com.kmcj.karte.resources.mold.maintenance.remodeling;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.inspection.result.TblMoldInspectionResultVo;
import com.kmcj.karte.resources.mold.issue.TblIssue;
import com.kmcj.karte.resources.mold.maintenance.detail.TblMoldMaintenanceDetailImageFileVo;
import com.kmcj.karte.resources.mold.maintenance.detail.TblMoldMaintenanceDetailVo;
import com.kmcj.karte.resources.mold.maintenance.part.TblMoldMaintencePartVo;
import com.kmcj.karte.resources.mold.maintenance.detail.TblMoldMaintenanceDetailPR;
import com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetailImageFileVo;
import com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetailVo;
import com.kmcj.karte.resources.mold.remodeling.inspection.TblMoldRemodelingInspectionResultVo;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import javax.validation.constraints.Null;

public class TblMoldMaintenanceRemodelingVo extends BasicResponse {

    private String id;
    private Integer mainteOrRemodel;
    private String mainteOrRemodelText;
    private String issueId;
    private String issueText;
    private String issueReportCategory1Id;
    private String issueReportCategory1Text;
    private String measureStatus;
    private Integer mainteType;
    private Integer remodelingType;
    private String remodelingTypeText;
    private String maintenanceId;
    private String mainteTypeText;
    private Date mainteDate;
    private String mainteDateText;
    private Date mainteDateStart;
    private Date mainteDateEnd;
    private String startDatetime;
    private String startDatetimeStz;
    private String endDatetime;
    private String endDatetimeStz;
    private String report;
    private String moldSpecHstId;
    private String moldSpecName;
    private String directionId;
    private String directionCode;
    private Date createDate;
    private Date updateDate;
    private String createUserUuid;
    private String updateUserUuid;
    private String moldUuid;
    private MstMold mstMold;
    private TblMoldMaintenanceRemodeling tblMoldMaintenanceRemodeling;
    private List<TblMoldMaintenanceRemodelingVo> moldMaintenanceRemodelingVo;
    private List<TblMoldMaintenanceDetailVo> moldMaintenanceDetailVo;
    private List<TblMoldRemodelingDetailVo> moldRemodelingDetailVo;
    private List<TblMoldInspectionResultVo> moldInspectionResultVo;
    private List<TblMoldRemodelingInspectionResultVo> moldRemodelingInspectionResultVo;
    private List<TblMoldMaintenanceDetailImageFileVo> imageFileVos;
    private List<TblMoldRemodelingDetailImageFileVo> rimageFileVos;
    private List<TblMoldMaintencePartVo> tblMoldMaintenancePartVO;
    @Null
    private List<TblMoldMaintenanceDetailPR> tblMoldlMaintenanceDetailPRVos;
    
    // 4.2 対応　BY LiuYoudong S
    private boolean resetAfterMainteTotalProducingTimeHourFlag;
    private boolean resetAfterMainteTotalShotCountFlag;
    private BigDecimal afterMainteTotalProducingTimeHour;
    private int afterMainteTotalShotCount;
    // 4.2 対応　BY LiuYoudong E

    private String moldId;
    private String moldName;
    private String reportPersonName;
    private String mainteStatus;
    private String externalFlg;
    
    // KM-361 メンテナンス所要時間の追加
    private int workingTimeMinutes;
    
    private long count;
    private int pageNumber;
    private int pageTotal;

    private TblIssue tblIssue;
    private Date measureDueDate;

	// メンテナンスの一次保存機能 2018/09/13 -S
    private int temporarilySaved;
	// メンテナンスの一次保存機能 2018/09/13 -E
    public TblMoldMaintenanceRemodelingVo() {
    }

    public TblMoldMaintenanceRemodelingVo(Date mainteDate, Integer mainteOrRemodel, String moldId, String moldName) {
        this.mainteDate = mainteDate;
        this.mainteOrRemodel = mainteOrRemodel;
        this.moldId = moldId;
        this.moldName = moldName;
    }

    public String getMoldId() {
        return moldId;
    }

    public void setMoldId(String moldId) {
        this.moldId = moldId;
    }

    public String getMoldName() {
        return moldName;
    }

    public void setMoldName(String moldName) {
        this.moldName = moldName;
    }

    public String getReportPersonName() {
        return reportPersonName;
    }

    public void setReportPersonName(String reportPersonName) {
        this.reportPersonName = reportPersonName;
    }

    public String getId() {
        return id;
    }

    public Integer getMainteOrRemodel() {
        return mainteOrRemodel;
    }
    
    public TblIssue getTblIssue() {
        return tblIssue;
    }
    
    public Date getMeasureDueDate() {
        return measureDueDate;
    }
    
    public String getIssueId() {
        return issueId;
    }

    public Integer getMainteType() {
        return mainteType;
    }

    public String getMainteTypeText() {
        return mainteTypeText;
    }

    public Date getMainteDate() {
        return mainteDate;
    }

    public String getStartDatetime() {
        return startDatetime;
    }

    public String getStartDatetimeStz() {
        return startDatetimeStz;
    }

    public String getEndDatetime() {
        return endDatetime;
    }

    public String getEndDatetimeStz() {
        return endDatetimeStz;
    }

    public String getReport() {
        return report;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public String getCreateUserUuid() {
        return createUserUuid;
    }

    public String getUpdateUserUuid() {
        return updateUserUuid;
    }

    public String getMoldUuid() {
        return moldUuid;
    }

    public MstMold getMstMold() {
        return mstMold;
    }

    public List<TblMoldMaintenanceRemodelingVo> getMoldMaintenanceRemodelingVo() {
        return moldMaintenanceRemodelingVo;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void setMainteOrRemodel(Integer mainteOrRemodel) {
        this.mainteOrRemodel = mainteOrRemodel;
    }

    public void setTblIssue(TblIssue tblIssue) {
        this.tblIssue = tblIssue;
    }
    
    public void setMeasureDueDate(Date measureDueDate) {
        this.measureDueDate = measureDueDate;
    }
    
    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public void setMainteType(Integer mainteType) {
        this.mainteType = mainteType;
    }

    public void setMainteTypeText(String mainteTypeText) {
        this.mainteTypeText = mainteTypeText;
    }

    public void setMainteDate(Date mainteDate) {
        this.mainteDate = mainteDate;
    }

    public void setStartDatetime(String startDatetime) {
        this.startDatetime = startDatetime;
    }

    public void setStartDatetimeStz(String startDatetimeStz) {
        this.startDatetimeStz = startDatetimeStz;
    }

    public void setEndDatetime(String endDatetime) {
        this.endDatetime = endDatetime;
    }

    public void setEndDatetimeStz(String endDatetimeStz) {
        this.endDatetimeStz = endDatetimeStz;
    }

    public void setReport(String report) {
        this.report = report;
    }

    public void setCreateDate(Date createDate) {
        this.createDate = createDate;
    }

    public void setUpdateDate(Date updateDate) {
        this.updateDate = updateDate;
    }

    public void setCreateUserUuid(String createUserUuid) {
        this.createUserUuid = createUserUuid;
    }

    public void setUpdateUserUuid(String updateUserUuid) {
        this.updateUserUuid = updateUserUuid;
    }

    public void setMoldUuid(String moldUuid) {
        this.moldUuid = moldUuid;
    }

    public void setMstMold(MstMold mstMold) {
        this.mstMold = mstMold;
    }

    public void setMoldMaintenanceRemodelingVo(List<TblMoldMaintenanceRemodelingVo> moldMaintenanceRemodelingVo) {
        this.moldMaintenanceRemodelingVo = moldMaintenanceRemodelingVo;
    }

    public Date getMainteDateStart() {
        return mainteDateStart;
    }

    public Date getMainteDateEnd() {
        return mainteDateEnd;
    }

    public void setMainteDateStart(Date mainteDateStart) {
        this.mainteDateStart = mainteDateStart;
    }

    public void setMainteDateEnd(Date mainteDateEnd) {
        this.mainteDateEnd = mainteDateEnd;
    }

    public void setMainteOrRemodelText(String mainteOrRemodelText) {
        this.mainteOrRemodelText = mainteOrRemodelText;
    }

    public void setMainteDateText(String mainteDateText) {
        this.mainteDateText = mainteDateText;
    }

    public String getMainteOrRemodelText() {
        return mainteOrRemodelText;
    }

    public String getMainteDateText() {
        return mainteDateText;
    }

    public List<TblMoldMaintenanceDetailVo> getMoldMaintenanceDetailVo() {
        return moldMaintenanceDetailVo;
    }

    public void setMoldMaintenanceDetailVo(List<TblMoldMaintenanceDetailVo> moldMaintenanceDetailVo) {
        this.moldMaintenanceDetailVo = moldMaintenanceDetailVo;
    }

    public List<TblMoldRemodelingDetailVo> getMoldRemodelingDetailVo() {
        return moldRemodelingDetailVo;
    }

    public void setMoldRemodelingDetailVo(List<TblMoldRemodelingDetailVo> moldRemodelingDetailVo) {
        this.moldRemodelingDetailVo = moldRemodelingDetailVo;
    }

    /**
     * @return the moldSpecHstId
     */
    public String getMoldSpecHstId() {
        return moldSpecHstId;
    }

    /**
     * @param moldSpecHstId the moldSpecHstId to set
     */
    public void setMoldSpecHstId(String moldSpecHstId) {
        this.moldSpecHstId = moldSpecHstId;
    }

    /**
     * @return the remodelingType
     */
    public Integer getRemodelingType() {
        return remodelingType;
    }

    /**
     * @param remodelingType the remodelingType to set
     */
    public void setRemodelingType(Integer remodelingType) {
        this.remodelingType = remodelingType;
    }

    public List<TblMoldInspectionResultVo> getMoldInspectionResultVo() {
        return moldInspectionResultVo;
    }

    public void setMoldInspectionResultVo(List<TblMoldInspectionResultVo> moldInspectionResultVo) {
        this.moldInspectionResultVo = moldInspectionResultVo;
    }

    public String getMaintenanceId() {
        return maintenanceId;
    }

    public void setMaintenanceId(String maintenanceId) {
        this.maintenanceId = maintenanceId;
    }

    public String getIssueReportCategory1Id() {
        return issueReportCategory1Id;
    }

    public String getIssueReportCategory1Text() {
        return issueReportCategory1Text;
    }

    public void setIssueReportCategory1Id(String issueReportCategory1Id) {
        this.issueReportCategory1Id = issueReportCategory1Id;
    }

    public void setIssueReportCategory1Text(String issueReportCategory1Text) {
        this.issueReportCategory1Text = issueReportCategory1Text;
    }

    public String getMoldSpecName() {
        return moldSpecName;
    }

    public void setMoldSpecName(String moldSpecName) {
        this.moldSpecName = moldSpecName;
    }

    public String getIssueText() {
        return issueText;
    }

    public void setIssueText(String issueText) {
        this.issueText = issueText;
    }

    /**
     * @return the remodelingTypeText
     */
    public String getRemodelingTypeText() {
        return remodelingTypeText;
    }

    /**
     * @param remodelingTypeText the remodelingTypeText to set
     */
    public void setRemodelingTypeText(String remodelingTypeText) {
        this.remodelingTypeText = remodelingTypeText;
    }

    public TblMoldMaintenanceRemodeling getTblMoldMaintenanceRemodeling() {
        return tblMoldMaintenanceRemodeling;
    }

    public void setTblMoldMaintenanceRemodeling(TblMoldMaintenanceRemodeling tblMoldMaintenanceRemodeling) {
        this.tblMoldMaintenanceRemodeling = tblMoldMaintenanceRemodeling;
    }

    /**
     * @return the externalFlg
     */
    public String getExternalFlg() {
        return externalFlg;
    }

    /**
     * @param externalFlg the externalFlg to set
     */
    public void setExternalFlg(String externalFlg) {
        this.externalFlg = externalFlg;
    }

    /**
     * @return the directionId
     */
    public String getDirectionId() {
        return directionId;
    }

    /**
     * @param directionId the directionId to set
     */
    public void setDirectionId(String directionId) {
        this.directionId = directionId;
    }

    /**
     * @return the directionCode
     */
    public String getDirectionCode() {
        return directionCode;
    }

    /**
     * @param directionCode the directionCode to set
     */
    public void setDirectionCode(String directionCode) {
        this.directionCode = directionCode;
    }

    /**
     * @return the mainteStatus
     */
    public String getMainteStatus() {
        return mainteStatus;
    }

    /**
     * @param mainteStatus the mainteStatus to set
     */
    public void setMainteStatus(String mainteStatus) {
        this.mainteStatus = mainteStatus;
    }

    /**
     * @return the moldRemodelingInspectionResultVo
     */
    public List<TblMoldRemodelingInspectionResultVo> getMoldRemodelingInspectionResultVo() {
        return moldRemodelingInspectionResultVo;
    }

    /**
     * @param moldRemodelingInspectionResultVo the moldRemodelingInspectionResultVo to set
     */
    public void setMoldRemodelingInspectionResultVo(List<TblMoldRemodelingInspectionResultVo> moldRemodelingInspectionResultVo) {
        this.moldRemodelingInspectionResultVo = moldRemodelingInspectionResultVo;
    }

    public void setImageFileVos(List<TblMoldMaintenanceDetailImageFileVo> imageFileVos) {
        this.imageFileVos = imageFileVos;
    }

    public List<TblMoldMaintenanceDetailImageFileVo> getImageFileVos() {
        return imageFileVos;
    }

    public List<TblMoldRemodelingDetailImageFileVo> getRimageFileVos() {
        return rimageFileVos;
    }

    public void setRimageFileVos(List<TblMoldRemodelingDetailImageFileVo> rimageFileVos) {
        this.rimageFileVos = rimageFileVos;
    }

    public List<TblMoldMaintenanceDetailPR> getTblMoldlMaintenanceDetailPRVos() {
        return tblMoldlMaintenanceDetailPRVos;
    }

    public void setTblMoldlMaintenanceDetailPRVos(List<TblMoldMaintenanceDetailPR> tblMoldlMaintenanceDetailPRVos) {
        this.tblMoldlMaintenanceDetailPRVos = tblMoldlMaintenanceDetailPRVos;
    }

    /**
     * @return the measureStatus
     */
    public String getMeasureStatus() {
        return measureStatus;
    }

    /**
     * @param measureStatus the measureStatus to set
     */
    public void setMeasureStatus(String measureStatus) {
        this.measureStatus = measureStatus;
    }

    /**
     * @return the resetAfterMainteTotalProducingTimeHourFlag
     */
    public boolean isResetAfterMainteTotalProducingTimeHourFlag() {
        return resetAfterMainteTotalProducingTimeHourFlag;
    }

    /**
     * @param resetAfterMainteTotalProducingTimeHourFlag the resetAfterMainteTotalProducingTimeHourFlag to set
     */
    public void setResetAfterMainteTotalProducingTimeHourFlag(boolean resetAfterMainteTotalProducingTimeHourFlag) {
        this.resetAfterMainteTotalProducingTimeHourFlag = resetAfterMainteTotalProducingTimeHourFlag;
    }

    /**
     * @return the resetAfterMainteTotalShotCountFlag
     */
    public boolean isResetAfterMainteTotalShotCountFlag() {
        return resetAfterMainteTotalShotCountFlag;
    }

    /**
     * @param resetAfterMainteTotalShotCountFlag the resetAfterMainteTotalShotCountFlag to set
     */
    public void setResetAfterMainteTotalShotCountFlag(boolean resetAfterMainteTotalShotCountFlag) {
        this.resetAfterMainteTotalShotCountFlag = resetAfterMainteTotalShotCountFlag;
    }

    /**
     * @return the workingTimeMinutes
     */
    public int getWorkingTimeMinutes() {
        return workingTimeMinutes;
    }

    /**
     * @param workingTimeMinutes the workingTimeMinutes to set
     */
    public void setWorkingTimeMinutes(int workingTimeMinutes) {
        this.workingTimeMinutes = workingTimeMinutes;
    }

    public long getCount() {
        return count;
    }

    public void setCount(long count) {
        this.count = count;
    }

    public int getPageNumber() {
        return pageNumber;
    }

    public void setPageNumber(int pageNumber) {
        this.pageNumber = pageNumber;
    }

    public int getPageTotal() {
        return pageTotal;
    }

    public void setPageTotal(int pageTotal) {
        this.pageTotal = pageTotal;
    }

    /**
     * @return the afterMainteTotalProducingTimeHour
     */
    public BigDecimal getAfterMainteTotalProducingTimeHour() {
        return afterMainteTotalProducingTimeHour;
    }

    /**
     * @param afterMainteTotalProducingTimeHour the afterMainteTotalProducingTimeHour to set
     */
    public void setAfterMainteTotalProducingTimeHour(BigDecimal afterMainteTotalProducingTimeHour) {
        this.afterMainteTotalProducingTimeHour = afterMainteTotalProducingTimeHour;
    }

    /**
     * @return the afterMainteTotalShotCount
     */
    public int getAfterMainteTotalShotCount() {
        return afterMainteTotalShotCount;
    }

    /**
     * @param afterMainteTotalShotCount the afterMainteTotalShotCount to set
     */
    public void setAfterMainteTotalShotCount(int afterMainteTotalShotCount) {
        this.afterMainteTotalShotCount = afterMainteTotalShotCount;
    }
    
    public List<TblMoldMaintencePartVo> getTblMoldMaintenancePartVO() {
        return tblMoldMaintenancePartVO;
    }

    public void setTblMoldMaintenancePartVO(List<TblMoldMaintencePartVo> tblMoldMaintenancePartVo) {
        this.tblMoldMaintenancePartVO = tblMoldMaintenancePartVo;
    }

	// メンテナンスの一次保存機能 2018/09/13 -S
    /**
     * @return the temporarilySaved
     */
    public int getTemporarilySaved() {
        return temporarilySaved;
    }

    /**
     * @param temporarilySaved the temporarilySaved to set
     */
    public void setTemporarilySaved(int temporarilySaved) {
        this.temporarilySaved = temporarilySaved;
    }

    // メンテナンスの一次保存機能 2018/09/13 -E
}
