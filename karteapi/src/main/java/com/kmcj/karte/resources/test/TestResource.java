/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.test;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.MailSender;
import com.kmcj.karte.batch.extercomponentinspection.ExternalComponentInspectionBatchlet;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.RequestParameters;

import com.kmcj.karte.properties.KartePropertyService;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.mold.MstMold;
import com.kmcj.karte.resources.mold.MstMoldList;
import com.kmcj.karte.resources.sigma.log.TblSigmaLog;
import com.kmcj.karte.util.DateFormat;
import com.kmcj.karte.util.FileUtil;
import com.kmcj.karte.util.TimezoneConverter;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Properties;
import javax.batch.operations.JobOperator;
import javax.batch.operations.JobSecurityException;
import javax.batch.operations.JobStartException;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.JobExecution;
import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;
import javax.servlet.ServletContext;
import javax.transaction.Transactional;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.UriInfo;
import javax.ws.rs.Consumes;
import javax.ws.rs.Produces;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PUT;
import javax.ws.rs.PathParam;
import javax.ws.rs.QueryParam;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import javax.mail.Address;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * REST Web Service
 *
 * @author f.kitaoji
 */
@Path("test")
@RequestScoped
public class TestResource {
    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Context
    private UriInfo context;

    //@Inject
    //private LoginUserStore loginUserStore;
    @Context
    private ContainerRequestContext requestContext;

    @Context
    private ServletContext servletContext;

    @Inject
    private KartePropertyService kartePropertyService;
    
    @Inject
    private MailSender mailSender;

    @Inject
    private TestService testService;

    /**
     * Creates a new instance of UsersResource
     */
    public TestResource() {
    }

    /**
     * Retrieves representation of an instance of com.kmcj.karte.TestResource
     *
     * @param apiToken
     * @return an instance of java.lang.String
     */
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public String getJson() {
        String apiToken = (String) requestContext.getProperty("APITOKEN");
        return "{\"Test\": \"Good Bye\", \"APITOKEN\":\"" + apiToken + "\"}";
    }

    @GET
    @Path("sigmatest")
    @Produces(MediaType.APPLICATION_JSON)
    public TblSigmaLog getSigmaTest() {
        Query query = entityManager.createQuery("SELECT s FROM TblSigmaLog s WHERE s.tblSigmaLogPK.createDate = :createDate");
        String str = "2017/02/17 12:35:45.834";
        query.setParameter("createDate", DateFormat.strToDateMill(str));
        TblSigmaLog sigma = (TblSigmaLog)query.getSingleResult();
        return sigma;
    }

    @POST
    @Path("sigmatest")
    @Transactional
    public BasicResponse postSigmaTest() {
        BasicResponse response = new BasicResponse();
        String str = "2017/02/17 12:35:45.834";
        TblSigmaLog sigma = new TblSigmaLog("1",1, DateFormat.strToDateMill(str));
        entityManager.persist(sigma);
        return response;
    }
    
    @GET
    @Path("moldtest")
    @Produces(MediaType.APPLICATION_JSON) 
    public MstMoldList getMstMoldList() {
        
        Query query = entityManager.createNamedQuery("MstMold.findAll");
        List<MstMold> list = query.getResultList();
        MstMoldList response = new MstMoldList();
        response.setMstMold(list);
        return response;
    }


    @POST
    @Path("converttime")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public TestDate covertTime(TestDate testDate) {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        String timeZone = loginUser.getJavaZoneId();
        testDate.setUserLocale(timeZone);
        testDate.setJstDate(TimezoneConverter.toSystemDefaultZoneTime(timeZone, testDate.getTestDate()));
        return testDate;
    }
    
    @GET
    @Path("userlocaltime")
//    @Produces(MediaType.TEXT_PLAIN)
    public String getUserLocalTime() {
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);
        return TimezoneConverter.getLocalTime(loginUser.getJavaZoneId()).toString();
    }

    @GET
    @Path("lang")
    public String getAcceptableLang() {
        List<Locale> acceptableLanguages = requestContext.getAcceptableLanguages();
        StringBuilder sb = new StringBuilder();
        for (Locale locale : acceptableLanguages) {
            sb.append(locale.toString());
            sb.append(",");
        }
        return sb.toString();
    }

    @GET
    @Path("documentdirectory")
    public String getDocumentDirectory() {
        return kartePropertyService.getDocumentDirectory();
    }

    /**
     * PUT method for updating or creating an instance of TestResource
     *
     * @param content representation for the resource
     */
    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    public void putJson(String content) {
    }
    
    /**
     * Execute batch.
     *
     * @param processMode
     * @return
     */
    @GET
    @Path("executeBatch/externalComponentInspectionBatchlet/{processMode}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response executeBatch(@PathParam("processMode") String processMode) {
        
        if (!(ExternalComponentInspectionBatchlet.PROCESS_MODE_ALL.equals(processMode)
                || ExternalComponentInspectionBatchlet.PROCESS_MODE_ITEM.equals(processMode)
                || ExternalComponentInspectionBatchlet.PROCESS_MODE_OUTGOING.equals(processMode)
                || ExternalComponentInspectionBatchlet.PROCESS_MODE_INCOMING.equals(processMode))) {
            return Response.status(Response.Status.BAD_REQUEST).build();
        }
        
        BatchStatus batchStatus;
        try {
            Properties jobProperties = new Properties();
            jobProperties.setProperty(ExternalComponentInspectionBatchlet.BATCH_PROPERTY_PROCESS_MODE, processMode);
            JobOperator jobOperator = BatchRuntime.getJobOperator();
            long executionId = jobOperator.start(ExternalComponentInspectionBatchlet.BATCH_NAME, jobProperties);
            batchStatus = checkStatus(jobOperator, executionId, 0);
        } catch (JobStartException | JobSecurityException ex) {
            batchStatus = BatchStatus.FAILED;
        }
        return Response.ok(batchStatus.toString()).build();
    }
    
    /**
     * Check batch status.
     *
     * @param jobOperator
     * @param executionId
     * @param retry
     * @return BatchStatus
     */
    private BatchStatus checkStatus(JobOperator jobOperator, long executionId, int retry) {
        int retryCheckParam = retry;
        JobExecution jobExecution = jobOperator.getJobExecution(executionId);
        BatchStatus status = jobExecution.getBatchStatus();

        if (status == BatchStatus.COMPLETED || status == BatchStatus.FAILED || status == BatchStatus.STOPPED) {
            return status;
        }
        if (retryCheckParam <= 30) {
            retryCheckParam++;
            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
            }
            return checkStatus(jobOperator, executionId, retryCheckParam);
        }
        return BatchStatus.FAILED;
    }
    
    /**
     * Mail Sending test
     * http://localhost/ws/karte/api/test/sendmail?host=smtp.kmc-j.com&port=587&username=*****&password=******&from=admin@mkarte.com&to=f.kitaoji@kmc-j.com
     * @param smtpHost
     * @param port
     * @param username
     * @param password
     * @param from
     * @param to
     * @return
     * @throws UnsupportedEncodingException 
     */
    @POST
    @Path("sendmail")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public BasicResponse sendMail(@QueryParam("host") String smtpHost, @QueryParam("port") String port,
            @QueryParam("username") String username,  @QueryParam("password") String password, 
            @QueryParam("from") String from, @QueryParam("to") String to
    ) throws UnsupportedEncodingException {
        BasicResponse response = new BasicResponse();
        String host = smtpHost; //"smtp.gmail.com";
        //String port = "587";
        String starttls = "false";
//final String charset = "ISO-2022-JP";
    final String charset = "UTF-8";

    final String encoding = "base64";
    // for local
    //String host = "localhost";
    //String port = "2525";
    //String starttls = "false";
        String subject = "Hello email";
        String content = "This is a test mail.";

        Properties props = new Properties();
        props.put("mail.smtp.host", host);
        props.put("mail.smtp.port", port);
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", starttls);

        props.put("mail.smtp.connectiontimeout", "10000");
        props.put("mail.smtp.timeout", "10000");

        props.put("mail.debug", "true");

        Session session = Session.getInstance(props, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            MimeMessage message = new MimeMessage(session);

              // Set From:
            message.setFrom(new InternetAddress(from, "M-Karte Administrator"));
              // Set ReplyTo:
            message.setReplyTo(new Address[]{new InternetAddress(from)});
              // Set To:
            message.setRecipient(Message.RecipientType.TO, new InternetAddress(to));

            message.setSubject(subject, charset);
            message.setText(content, charset);

            message.setHeader("Content-Transfer-Encoding", encoding);

            Transport.send(message);

        } catch (MessagingException e) {
            response.setError(true);
            response.setErrorMessage(e.toString());
        } catch (UnsupportedEncodingException e) {
            response.setError(true);
            response.setErrorMessage(e.toString());
        }        
        
        return response;
    }
    
    /**
     * sendMailTest
     *
     *
     * @return
     * @throws UnsupportedEncodingException
     */
    @POST
    @Path("sendmailtest")
    public BasicResponse sendMailTest() throws UnsupportedEncodingException {

        BasicResponse response = new BasicResponse();
        
        List<String> receiverList = new ArrayList();
        List<String> ccList = new ArrayList();
        
        receiverList.add("pengguodong@zdsoft.cn");
       
        try {
            //MailUtil.sendMail(kartePropertyService, "mkarte-admin@kmc-j.com", "M-Karte Administrator", "n,liuyoudong@zdsoft.cn", "メール送信用のテスト", "メール送信成功！");
            mailSender.sendMail("mkarte-admin@kmc-j.com", "M-Karte Administrator", receiverList, null, "メール送信用のテスト", "メール送信成功！");

        } catch (IOException | MessagingException e) {
            response.setError(true);
            response.setErrorMessage(e.toString());
        }

        return response;
    }
    
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    @Path("encrypt/{txt}")
    public BasicResponse encrypt(@PathParam("txt") String txt) {
        BasicResponse response = new BasicResponse();
        System.out.println(txt + " was encrypted to " + FileUtil.encrypt(txt));
        response.setErrorMessage("This API doesn't return any response. Only for debug use.");
        return response;
    }
}
