/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.sigma;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.util.FileUtil;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

/**
 *
 * @author jiangxs
 */
@RequestScoped
@Path("sigma")
public class MstSigmaResource {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstSigmaService mstSigmaService;

    @Context
    private ContainerRequestContext requestContext;
    
    @Inject
    private KartePropertyService kartePropertyService;

    public MstSigmaResource() {

    }

    /**
     * sigmaデータ取得
     * @param machineId 設備ID
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstSigmaList getSigma(@QueryParam("machineId") String machineId) {
        MstSigmaList response = mstSigmaService.getSigma(machineId);
        return response;
    }
    
    /**
     * sigmaデータ取得
     * @param sigmaCode 
     * @return
     */
    @GET
    @Path("gunshi")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstSigmaList getSigmaValue(@QueryParam("sigmaCode") String sigmaCode) {
        MstSigmaList response = mstSigmaService.getSigmaValue(sigmaCode);
        return response;
    }
    
    /**
     * Σ軍師ユーザー、パスワード変更
     * @param mstSigma
     * @return 
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("changepwd")
    public BasicResponse updateSigmaPassword(MstSigma mstSigma) {
        BasicResponse response = new BasicResponse();
        try {
            mstSigmaService.updateSigmaPassword(mstSigma);
        }
        catch (Exception e) {
            response.setError(true);
            response.setErrorCode(ErrorMessages.E201_APPLICATION);
            response.setErrorMessage(e.getMessage());
        }
        return response;
    }
    
    /**
     * M1101 Σ軍師登録 編集された内容でΣ軍師マスタに追加・更新・削除を実行する。
     *
     * @param mstSigmaList
     * @return
     */
    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse postMstSigmas(MstSigmaList mstSigmaList) {
        BasicResponse response = new BasicResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        response = mstSigmaService.postMstSigmas(mstSigmaList, loginUser);
        return response;
    }

    /**
     * getSigmaMachine軍師と設備関連データ取得
     *
     * @param sigmaCode
     * @return
     */
    @GET
    @Path("sigmamachine")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstSigmaList getSigmaMachine(@QueryParam("sigmaCode") String sigmaCode) {

        MstSigmaList response = new MstSigmaList();

        try {

            response = mstSigmaService.getSigmaMachine(sigmaCode);

        } catch (Exception e) {// 異常処理

            response.setError(true);

            response.setErrorMessage(e.toString());

            return response;

        }

        return response;
    }
    
    /**
     * checkSigmaViewerVersion
     *
     * @param sigmaViewerVersion
     * @return
     */
    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Path("checksigmaviewerversion")
    public BasicResponse checkSigmaViewerVersion(@QueryParam("sigmaViewerVersion") String sigmaViewerVersion) {
        BasicResponse response = new BasicResponse();
        StringBuilder filePath = new StringBuilder(kartePropertyService.getDocumentDirectory());
        filePath.append(FileUtil.SEPARATOR).append(CommonConstants.MODULE);
        filePath.append(FileUtil.SEPARATOR).append("sigmaviewerversion");
        FileReader fr = null;
        BufferedReader br = null;
        try {
            File file = new File(filePath.toString());
            fr = new FileReader(file);
            br = new BufferedReader(fr);
            String ver = br.readLine();
            if (ver != null && sigmaViewerVersion != null && !ver.equals(sigmaViewerVersion)) {
                response.setError(true);
                response.setErrorCode(ErrorMessages.E901_OTHER);
            } else if (ver != null && sigmaViewerVersion != null && ver.equals(sigmaViewerVersion)) {
                response.setError(false);
            } else{
                response.setError(true);
                response.setErrorCode(ErrorMessages.E901_OTHER);
            }
        }
        catch (FileNotFoundException nf) {
            response.setError(true);
            response.setErrorCode("Version file not found.");
        }
        catch (IOException ie) {
            response.setError(true);
            response.setErrorCode("IO error encountered.");
        }
        finally {
            if (br != null) {
                try {
                    br.close();
                }
                catch (IOException ie) {}
            }
            if (fr != null) {
                try {
                    fr.close();
                }
                catch (IOException io) {}
            }
        }
        return response;
    }
    
}
