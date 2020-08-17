package com.kmcj.karte.resources.component.registration;

import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.RequestParameters;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.component.MstComponent;
import com.kmcj.karte.resources.component.MstComponentService;
import com.kmcj.karte.resources.component.attribute.MstComponentAttribute;
import com.kmcj.karte.resources.component.attribute.MstComponentAttributeService;
import com.kmcj.karte.resources.component.spec.MstComponentSpec;
import com.kmcj.karte.resources.component.spec.MstComponentSpecService;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.IDGenerator;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.ws.rs.*;
import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.List;

/**
 * @author bacpd
 */
@RequestScoped
@Path("component/registration")
public class MstComponentRegistrationResource {
    @Context
    private ContainerRequestContext requestContext;

    @Inject
    private MstDictionaryService mstDictionaryService;

    @Inject
    private MstComponentRegistrationService mstComponentRegistrationService;

    @Inject
    private MstComponentService mstComponentService;

    @Inject
    private MstComponentAttributeService mstComponentAttributeService;

    @Inject
    private MstComponentSpecService mstComponentSpecService;

    public MstComponentRegistrationResource() {

    }

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public MstComponentRegistrationResponse registerComponentInspection(List<MstComponentRegistrationList> mstComponentRegistrationLists) {
        MstComponentRegistrationResponse response = new MstComponentRegistrationResponse();
        LoginUser loginUser = (LoginUser) requestContext.getProperty(RequestParameters.LOGINUSER);

        List<MstComponentFailedParts> listFailPart = new ArrayList<>();

        for (MstComponentRegistrationList componentRegistration : mstComponentRegistrationLists) {
            if (!mstComponentRegistrationService.validateComponentAttribute(componentRegistration.getComponentCode(), 45)
                    || !mstComponentRegistrationService.validateComponentAttribute(componentRegistration.getComponentName(), 100)) {
                MstComponentFailedParts failedParts = new MstComponentFailedParts();
                failedParts.componentCode = componentRegistration.getComponentCode();
                if (componentRegistration.getComponentCode().isEmpty()) {
                    failedParts.reason = mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "component_registration_required");
                } else {
                    failedParts.reason = CommonConstants.INVALID_FIELD_TYPE;
                }
                listFailPart.add(failedParts);

            } else {
                String uId = IDGenerator.generate();
                MstComponent componentExists = mstComponentService.getMstComponentByCode(componentRegistration.getComponentCode());
                if (componentExists != null) {
                    mstComponentRegistrationService.updateComponent(componentRegistration, loginUser);
                    uId = componentExists.getId();
                } else {
                    mstComponentRegistrationService.insertComponent(componentRegistration, loginUser, uId);
                }

                //check attr_id exist at mst_component_attribute.id
                for (MstComponentSpecList componentSpec : componentRegistration.getMstComponentSpecCollection()) {
                    MstComponentAttribute mstAttributeServiceExists = mstComponentAttributeService.getMstComponentAttributeByName(componentSpec.getAttrName());
                    if (mstAttributeServiceExists != null) {
                        //if exist check mst_component_spec with 2 PK
                        MstComponentSpec componentSpecExists = mstComponentSpecService.getMstComponentSpec(uId, mstAttributeServiceExists.getId());
                        if (componentSpecExists != null) {
                            mstComponentSpecService.updateMstComponentSpecRegistration(uId, mstAttributeServiceExists.getId(), componentSpec.getAttrValue(), loginUser);
                        } else {
                            mstComponentSpecService.insertMstComponentSpecRegistration(uId , mstAttributeServiceExists.getId(), componentSpec.getAttrValue(), loginUser);
                        }
                    }
                }
            }
        }

        response.setFailedParts(listFailPart);

        return response;
    }
}
