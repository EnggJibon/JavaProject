/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.spec;

import com.kmcj.karte.FileReponse;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.util.CSVFileUtil;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.List;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.core.Context;
import javax.ws.rs.Produces;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author hangju
 */
@RequestScoped
@Path("component/spec")
public class MstComponentSpecResource {

    /**
     * Creates a new instance of MstComponentSpecResource
     */
    public MstComponentSpecResource() {
    }

    @Inject
    private MstComponentSpecService mstComponentSpecService;

    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private KartePropertyService kartePropertyService;

    @GET
    @Path("exportcsv")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public FileReponse getComponentSpecCSV(@QueryParam("componentCode") String componentCode,
            @QueryParam("componentType") String componentType) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return mstComponentSpecService.getMstComponentSpecOutputCsv(componentCode, componentType, loginUser);
    }

    /**
     * 部品詳細でCSV取込する用
     *
     * @param fileUuid
     * @return
     */
    @POST
    @Path("importcsv/{fileUuid}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentSpecList getComponentDetailByCsv(@PathParam("fileUuid") String fileUuid) {
        String csvFile = FileUtil.getCsvFilePath(kartePropertyService, fileUuid);
        CSVFileUtil csvFileUtil = null;
        long failedCount = 0;
        FileUtil fu = new FileUtil();

        ArrayList readList = new ArrayList();
        try {
            csvFileUtil = new CSVFileUtil(csvFile);
            boolean readEnd = false;
            do {
                String readLine = csvFileUtil.readLine();
                if (readLine == null) {
                    readEnd = true;
                } else {
                    readList.add(CSVFileUtil.fromCSVLinetoArray(readLine));
                }
            } while (!readEnd);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            // CSVファイルwriterのクローズ処理
            if (csvFileUtil != null) {
                csvFileUtil.close();
            }
        }
        MstComponentSpecList mstComponentSpecList = new MstComponentSpecList();

        if (readList.size() <= 1) {
            List<MstComponentAttrSpecVo> readCsvList = new ArrayList<MstComponentAttrSpecVo>();
            mstComponentSpecList.setCsvMstComponentAttrSpecVo(readCsvList);
            return mstComponentSpecList;
        } else {
            String logFileUuid = IDGenerator.generate();
            String logFile = FileUtil.getLogFilePath(kartePropertyService, logFileUuid);
            List<MstComponentAttrSpecVo> readCsvList = new ArrayList<MstComponentAttrSpecVo>();
            for (int i = 1; i < readList.size(); i++) {
                ArrayList comList = (ArrayList) readList.get(i);                
                if (comList.size() < 3) {
                    //エラー情報をログファイルに記入
//                    fu.writeInfoToFile(logFile, fu.outValue(i, i, "", "", error, 1, errorContents, layout));
                    failedCount += 1;
                    continue;
                }
                
                MstComponentAttrSpecVo mstComponentAttrSpecVo = new MstComponentAttrSpecVo();
                mstComponentAttrSpecVo.setAttrCode(String.valueOf(comList.get(0)));
                mstComponentAttrSpecVo.setAttrName(String.valueOf(comList.get(1)));
                mstComponentAttrSpecVo.setAttrValue(String.valueOf(comList.get(2)));
                readCsvList.add(mstComponentAttrSpecVo);
            }
            mstComponentSpecList.setCsvMstComponentAttrSpecVo(readCsvList);
            return mstComponentSpecList;
        }
    }

}
