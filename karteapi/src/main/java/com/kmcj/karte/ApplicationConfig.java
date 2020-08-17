/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte;

import java.util.Set;
import javax.ws.rs.core.Application;

import org.glassfish.jersey.media.multipart.MultiPartFeature;

/**
 *
 * @author f.kitaoji
 */
@javax.ws.rs.ApplicationPath("api")
public class ApplicationConfig extends Application {

    @Override
    public Set<Class<?>> getClasses() {
        Set<Class<?>> resources = new java.util.HashSet<>();
        addRestResourceClasses(resources);
        resources.add(MultiPartFeature.class);
        return resources;
    }

    /**
     * Do not modify addRestResourceClasses() method. It is automatically
     * populated with all resources defined in the project. If required, comment
     * out calling this method in getClasses().
     */
    private void addRestResourceClasses(Set<Class<?>> resources) {
        resources.add(com.kmcj.karte.ObjectResponseBodyWriter.class);
        resources.add(com.kmcj.karte.batch.externalmold.delete.TblDeletedKeyResource.class);
        resources.add(com.kmcj.karte.conf.CnfSystemResource.class);
        resources.add(com.kmcj.karte.conf.application.CnfApplicationResource.class);
        resources.add(com.kmcj.karte.filters.AuthenticationFilter.class);
        resources.add(com.kmcj.karte.filters.AuthorizationFilter.class);
        resources.add(com.kmcj.karte.filters.ResponseFilter.class);
        resources.add(com.kmcj.karte.resources.apiuser.MstApiUserResource.class);
        resources.add(com.kmcj.karte.resources.asset.MstAssetResource.class);
        resources.add(com.kmcj.karte.resources.asset.disposal.AssetDisposalRequestNoticeResource.class);
        resources.add(com.kmcj.karte.resources.asset.disposal.TblAssetDisposalResource.class);
        resources.add(com.kmcj.karte.resources.asset.inventory.TblInventoryResource.class);
        resources.add(com.kmcj.karte.resources.asset.inventory.detail.TblInventoryDetailResource.class);
        resources.add(com.kmcj.karte.resources.asset.inventory.mgmt.company.TblInventoryMgmtCompanyResource.class);
        resources.add(com.kmcj.karte.resources.asset.inventory.request.TblInventoryRequestResource.class);
        resources.add(com.kmcj.karte.resources.asset.inventory.request.detail.TblInventoryRequestDetailResource.class);
        resources.add(com.kmcj.karte.resources.asset.match.TblAssetMatchingResource.class);
        resources.add(com.kmcj.karte.resources.authctrl.MstAuhCtrlResource.class);
        resources.add(com.kmcj.karte.resources.authentication.AuthenticationResource.class);
        resources.add(com.kmcj.karte.resources.authorization.MstAuthResource.class);
        resources.add(com.kmcj.karte.resources.choice.MstChoiceResource.class);
        resources.add(com.kmcj.karte.resources.choice.category.MstChoiceCategoryResource.class);
        resources.add(com.kmcj.karte.resources.circuitboard.automaticmachine.log.TblAutomaticMachineLogResource.class);
        resources.add(com.kmcj.karte.resources.circuitboard.defect.MstCircuitBoardDefectResource.class);
        resources.add(com.kmcj.karte.resources.circuitboard.defect.inspection.detail.DefectInspectionResource.class);
        resources.add(com.kmcj.karte.resources.circuitboard.defect.procedure.detail.DefectProcedureResource.class);
        resources.add(com.kmcj.karte.resources.circuitboard.inspection.TblCircuitBoardInspectionResultResource.class);
        resources.add(com.kmcj.karte.resources.circuitboard.inspection.TblCircuitBoardInspectionResultSumResource.class);
        resources.add(com.kmcj.karte.resources.circuitboard.inspectorManhours.TblInspectorManhoursResouce.class);
        resources.add(com.kmcj.karte.resources.circuitboard.point.MstCircuitBoardPointResource.class);
        resources.add(com.kmcj.karte.resources.circuitboard.prifix.MstCircuitNamePrefixResource.class);
        resources.add(com.kmcj.karte.resources.circuitboard.procedure.MstCircuitBoardProcedureResource.class);
        resources.add(com.kmcj.karte.resources.circuitboard.procedure.retention.ProcedureRetentionResource.class);
        resources.add(com.kmcj.karte.resources.circuitboard.product.MstProductResource.class);
        resources.add(com.kmcj.karte.resources.circuitboard.productionline.MstProductionLineResource.class);
        resources.add(com.kmcj.karte.resources.circuitboard.productionline.machine.MstProductionLineMachineResource.class);
        resources.add(com.kmcj.karte.resources.circuitboard.serialnumber.MstCircuitBoardSerialNumberResource.class);
        resources.add(com.kmcj.karte.resources.circuitboard.smt.cassette.SMTCassetteResource.class);
        resources.add(com.kmcj.karte.resources.circuitboard.smt.nozzle.SMTnozzleResource.class);
        resources.add(com.kmcj.karte.resources.circuitboard.smt.productline.SMTproductionResource.class);
        resources.add(com.kmcj.karte.resources.circuitboard.targetppm.MstCircuitBoardTargetPpmResouce.class);
        resources.add(com.kmcj.karte.resources.common.gridcolumn.GridcolumnMemoryResource.class);
        resources.add(com.kmcj.karte.resources.common.search.TblSearchCondMemoryResource.class);
        resources.add(com.kmcj.karte.resources.company.MstCompanyResource.class);
        resources.add(com.kmcj.karte.resources.component.MstComponentResource.class);
        resources.add(com.kmcj.karte.resources.component.attribute.MstComponentAttributeResource.class);
        resources.add(com.kmcj.karte.resources.component.company.MstComponentCompanyResource.class);
        resources.add(com.kmcj.karte.resources.component.inspection.defect.TblComponentInspectionDefectResource.class);
        resources.add(com.kmcj.karte.resources.component.inspection.file.MstComponentInspectFileResource.class);
        resources.add(com.kmcj.karte.resources.component.inspection.item.MstComponentInspectionItemsTableResource.class);
        resources.add(com.kmcj.karte.resources.component.inspection.referencefile.TblComponentInspectionReferenceFileResource.class);
        resources.add(com.kmcj.karte.resources.component.inspection.result.TblComponentInspectionResultResource.class);
        resources.add(com.kmcj.karte.resources.component.inspection.visualimage.TblComponentInspectionVisualImageResource.class);
        resources.add(com.kmcj.karte.resources.component.lot.TblComponentLotResource.class);
        resources.add(com.kmcj.karte.resources.component.lot.acceptance.TblComponentLotAcceptanceReportResource.class);
        resources.add(com.kmcj.karte.resources.component.lot.relation.TblComponentLotRelationResource.class);
        resources.add(com.kmcj.karte.resources.component.material.MstComponentMaterialResource.class);
        resources.add(com.kmcj.karte.resources.component.registration.MstComponentRegistrationResource.class);
        resources.add(com.kmcj.karte.resources.component.spec.MstComponentSpecResource.class);
        resources.add(com.kmcj.karte.resources.component.structure.MstComponentStructureResource.class);
        resources.add(com.kmcj.karte.resources.contact.MstContactResource.class);
        resources.add(com.kmcj.karte.resources.currency.MstCurrencyResource.class);
        resources.add(com.kmcj.karte.resources.custom.dashboard.CustomDashboardResource.class);
        resources.add(com.kmcj.karte.resources.custom.report.TblCustomReportQueryResource.class);
        resources.add(com.kmcj.karte.resources.custom.report.category.TblCustomReportCategoryResource.class);
        resources.add(com.kmcj.karte.resources.custom.report.user.MstQueryUserResource.class);
        resources.add(com.kmcj.karte.resources.dictionary.MstDictionaryResource.class);
        resources.add(com.kmcj.karte.resources.direction.TblDirectionResource.class);
        resources.add(com.kmcj.karte.resources.external.MstExternalDataGetSettingResource.class);
        resources.add(com.kmcj.karte.resources.fileimportjob.FileImportJobResource.class);
        resources.add(com.kmcj.karte.resources.filelinkptn.MstFileLinkPtnResource.class);
        resources.add(com.kmcj.karte.resources.files.FileResource.class);
        resources.add(com.kmcj.karte.resources.function.MstFunctionResource.class);
        resources.add(com.kmcj.karte.resources.installationsite.MstInstallationSiteResource.class);
        resources.add(com.kmcj.karte.resources.item.MstItemResource.class);
        resources.add(com.kmcj.karte.resources.language.MstLanguageResource.class);
        resources.add(com.kmcj.karte.resources.location.MstLocationResource.class);
        resources.add(com.kmcj.karte.resources.machine.MstMachineResource.class);
        resources.add(com.kmcj.karte.resources.machine.assetno.MstMachineAssetNoResource.class);
        resources.add(com.kmcj.karte.resources.machine.attribute.MstMachineAttributeResource.class);
        resources.add(com.kmcj.karte.resources.machine.daily.report.TblMachineDailyReportResource.class);
        resources.add(com.kmcj.karte.resources.machine.dailyreport2.MachineDailyReport2Resource.class);
        resources.add(com.kmcj.karte.resources.machine.dailyreport2.bulk.BulkMachDailyReportResource.class);
        resources.add(com.kmcj.karte.resources.machine.downtime.MstMachineDowntimeResource.class);
        resources.add(com.kmcj.karte.resources.machine.filedef.MstMachineFileDefResource.class);
        resources.add(com.kmcj.karte.resources.machine.history.TblMachineHistoryResource.class);
        resources.add(com.kmcj.karte.resources.machine.inspection.choice.MstMachineInspectionChoiceResource.class);
        resources.add(com.kmcj.karte.resources.machine.inspection.item.MstMachineInspectionItemResource.class);
        resources.add(com.kmcj.karte.resources.machine.inspection.result.TblMachineInspectionResultResource.class);
        resources.add(com.kmcj.karte.resources.machine.inventory.TblMachineInventoryResource.class);
        resources.add(com.kmcj.karte.resources.machine.location.history.TblMachineLocationHistoryResource.class);
        resources.add(com.kmcj.karte.resources.machine.maintenance.detail.TblMachineMaintenanceDetailResource.class);
        resources.add(com.kmcj.karte.resources.machine.maintenance.recomend.TblMachineMaintenanceRecomendResource.class);
        resources.add(com.kmcj.karte.resources.machine.maintenance.remodeling.TblMachineMaintenanceRemodelingResource.class);
        resources.add(com.kmcj.karte.resources.machine.operating.rate.TblMachineOperatingRateResource.class);
        resources.add(com.kmcj.karte.resources.machine.proccond.MstMachineProcCondResource.class);
        resources.add(com.kmcj.karte.resources.machine.proccond.attribute.MstMachineProcCondAttributeResource.class);
        resources.add(com.kmcj.karte.resources.machine.proccond.spec.MstMachineProcCondSpecResource.class);
        resources.add(com.kmcj.karte.resources.machine.production.TblMachineProductionPeriodResource.class);
        resources.add(com.kmcj.karte.resources.machine.reception.TblMachineReceptionResource.class);
        resources.add(com.kmcj.karte.resources.machine.remodeling.detail.TblMachineRemodelingDetailResource.class);
        resources.add(com.kmcj.karte.resources.machine.remodeling.inspection.TblMachineRemodelingInspectionResultResource.class);
        resources.add(com.kmcj.karte.resources.machine.spec.MstMachineSpecResource.class);
        resources.add(com.kmcj.karte.resources.machine.spec.history.MstMachineSpecHistoryResource.class);
        resources.add(com.kmcj.karte.resources.maintenance.cycleptn.TblMaintenanceCyclePtnResource.class);
        resources.add(com.kmcj.karte.resources.material.MstMaterialResource.class);
        resources.add(com.kmcj.karte.resources.material.lot.TblMaterialLotResource.class);
        resources.add(com.kmcj.karte.resources.material.stock.TblMaterialStockResource.class);
        resources.add(com.kmcj.karte.resources.material.stock.detail.TblMaterialStockDetailResource.class);
        resources.add(com.kmcj.karte.resources.mgmt.company.MstMgmtCompanyResource.class);
        resources.add(com.kmcj.karte.resources.mgmt.location.MstMgmtLocationResource.class);
        resources.add(com.kmcj.karte.resources.mold.MstMoldResource.class);
        resources.add(com.kmcj.karte.resources.mold.assetno.MstMoldAssetNoResource.class);
        resources.add(com.kmcj.karte.resources.mold.attribute.MstMoldAttributeResource.class);
        resources.add(com.kmcj.karte.resources.mold.component.relation.MstMoldComponentRelationResource.class);
        resources.add(com.kmcj.karte.resources.mold.inspection.choice.MstMoldInspectionChoiceResource.class);
        resources.add(com.kmcj.karte.resources.mold.inspection.item.MstMoldInspectionItemResource.class);
        resources.add(com.kmcj.karte.resources.mold.inspection.result.TblMoldInspectionResultResource.class);
        resources.add(com.kmcj.karte.resources.mold.inventory.TblMoldInventoryResource.class);
        resources.add(com.kmcj.karte.resources.mold.issue.TblIssueResource.class);
        resources.add(com.kmcj.karte.resources.mold.location.history.TblMoldLocationHistoryResource.class);
        resources.add(com.kmcj.karte.resources.mold.maintenance.detail.TblMoldMaintenanceDetailResource.class);
        resources.add(com.kmcj.karte.resources.mold.maintenance.recomend.TblMoldMaintenanceRecomendResource.class);
        resources.add(com.kmcj.karte.resources.mold.maintenance.remodeling.TblMoldMaintenanceRemodelingResource.class);
        resources.add(com.kmcj.karte.resources.mold.part.MstMoldPartResource.class);
        resources.add(com.kmcj.karte.resources.mold.part.changehistory.MPChangePrintHistoryResource.class);
        resources.add(com.kmcj.karte.resources.mold.part.order.MoldPartOrderResource.class);
        resources.add(com.kmcj.karte.resources.mold.part.rel.MstMoldPartRelResource.class);
        resources.add(com.kmcj.karte.resources.mold.part.stock.MoldPartStockResource.class);
        resources.add(com.kmcj.karte.resources.mold.part.stock.inout.MoldPartStockInOutResource.class);
        resources.add(com.kmcj.karte.resources.mold.proccond.MstMoldProcCondResource.class);
        resources.add(com.kmcj.karte.resources.mold.proccond.attribute.MstMoldProcCondAttributeResource.class);
        resources.add(com.kmcj.karte.resources.mold.proccond.spec.MstMoldProcCondSpecResource.class);
        resources.add(com.kmcj.karte.resources.mold.production.TblMoldProductionPeriodResource.class);
        resources.add(com.kmcj.karte.resources.mold.reception.TblMoldReceptionResource.class);
        resources.add(com.kmcj.karte.resources.mold.remodeling.detail.TblMoldRemodelingDetailResource.class);
        resources.add(com.kmcj.karte.resources.mold.remodeling.inspection.TblMoldRemodelingInspectionResultResource.class);
        resources.add(com.kmcj.karte.resources.mold.spec.MstMoldSpecResource.class);
        resources.add(com.kmcj.karte.resources.mold.spec.history.MstMoldSpecHistoryResource.class);
        resources.add(com.kmcj.karte.resources.operation.MstOperationResource.class);
        resources.add(com.kmcj.karte.resources.operation.log.TblOperationLogResource.class);
        resources.add(com.kmcj.karte.resources.password.policy.CnfPasswordPolicyResource.class);
        resources.add(com.kmcj.karte.resources.po.TblPoResource.class);
        resources.add(com.kmcj.karte.resources.po.shipment.TblShipmentResource.class);
        resources.add(com.kmcj.karte.resources.poqr.MstPoQrResource.class);
        resources.add(com.kmcj.karte.resources.procedure.MstProcedureResource.class);
        resources.add(com.kmcj.karte.resources.production.TblProductionResource.class);
        resources.add(com.kmcj.karte.resources.production.defect.TblProductionDefectResource.class);
        resources.add(com.kmcj.karte.resources.production.detail.TblProductionDetailResource.class);
        resources.add(com.kmcj.karte.resources.production.lot.balance.TblProductionLotBalanceResource.class);
        resources.add(com.kmcj.karte.resources.production.plan.TblProductionPlanResource.class);
        resources.add(com.kmcj.karte.resources.production.stock.TblProductionStockResource.class);
        resources.add(com.kmcj.karte.resources.production.suspension.TblProductionSuspensionResource.class);
        resources.add(com.kmcj.karte.resources.sigma.MstSigmaResource.class);
        resources.add(com.kmcj.karte.resources.sigma.imp.SigmaImportResource.class);
        resources.add(com.kmcj.karte.resources.sigma.log.TblSigmaLogResource.class);
        resources.add(com.kmcj.karte.resources.sigma.log.backup.SigmaLogBackupResource.class);
        resources.add(com.kmcj.karte.resources.sigma.threshold.MstSigmaThresholdResource.class);
        resources.add(com.kmcj.karte.resources.sigma.thtransport.TblProductionThsetResource.class);
        resources.add(com.kmcj.karte.resources.standard.worktime.MstStandardWorkTimeResource.class);
        resources.add(com.kmcj.karte.resources.stock.TblStockResource.class);
        resources.add(com.kmcj.karte.resources.stock.detail.TblStockDetailResource.class);
        resources.add(com.kmcj.karte.resources.test.TestResource.class);
        resources.add(com.kmcj.karte.resources.timezone.MstTimezoneResource.class);
        resources.add(com.kmcj.karte.resources.upload.TblCsvUploadErrorResource.class);
        resources.add(com.kmcj.karte.resources.user.MstUserResource.class);
        resources.add(com.kmcj.karte.resources.user.mail.reception.MstUserMailReceptionResource.class);
        resources.add(com.kmcj.karte.resources.work.TblWorkResource.class);
        resources.add(com.kmcj.karte.resources.work.close.entry.TblWorkCloseEntryResource.class);
        resources.add(com.kmcj.karte.resources.work.phase.MstWorkPhaseResource.class);
    }

}
