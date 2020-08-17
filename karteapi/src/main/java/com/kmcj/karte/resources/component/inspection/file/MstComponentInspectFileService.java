/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.resources.component.inspection.file;

import com.kmcj.karte.BasicResponse;
import com.kmcj.karte.constants.CommonConstants;
import com.kmcj.karte.constants.ErrorMessages;
import com.kmcj.karte.resources.authentication.LoginUser;
import com.kmcj.karte.resources.choice.MstChoiceService;
import com.kmcj.karte.resources.company.MstCompany;
import com.kmcj.karte.resources.company.MstCompanyService;
import com.kmcj.karte.resources.component.inspection.file.model.MstComponentInspectClassList;
import com.kmcj.karte.resources.component.inspection.file.model.MstComponentInspectClassVo;
import com.kmcj.karte.resources.component.inspection.file.model.MstComponentInspectFileVo;
import com.kmcj.karte.resources.component.inspection.file.model.MstComponentInspectFileList;
import com.kmcj.karte.resources.component.inspection.file.model.MstComponentInspectTypeList;
import com.kmcj.karte.resources.component.inspection.file.model.MstComponentInspectTypeVo;
import com.kmcj.karte.resources.component.inspection.file.model.MstFileSettingsForBat;
import com.kmcj.karte.resources.component.inspection.item.model.MstComponentInspectItemsTableClassList;
import com.kmcj.karte.resources.component.inspection.item.model.MstComponentInspectionItemsTableClassVo;
import com.kmcj.karte.resources.dictionary.MstDictionaryService;
import com.kmcj.karte.util.IDGenerator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javax.enterprise.context.Dependent;
import javax.inject.Inject;
import javax.persistence.*;
import javax.transaction.Transactional;
import org.apache.commons.lang.StringUtils;

/**
 *
 * @author Apeng
 */
@Dependent
public class MstComponentInspectFileService {

    private static final String INSPECT_TYPE = "inspect_type_";
    private static final String LANG_ID = "zh,ja,en";
    private static final String STRING_FILE = "|";

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    EntityManager entityManager;

    @Inject
    private MstDictionaryService mstDictionaryService;
    
    @Inject
    private MstChoiceService mstChoiceService;
    
    @Inject
    private MstCompanyService mstCompanyService;

    /**
     * 検査ファイル種類設定取得
     *
     * @param langId
     * @return
     */
    public MstComponentInspectFileList getMstComponentInspectFileVoList(String langId) {

        MstComponentInspectFileList mstComponentInspectFileVoList = new MstComponentInspectFileList();

        //検査ファイル種類設定テーブル
        List<MstComponentInspectType> list = getFileSql(langId);
        List<MstComponentInspectTypeVo> mstComponentInspectTypeVos = new ArrayList();
        MstComponentInspectTypeVo mstComponentInspectTypeVo;
        List<MstComponentInspectClass> listClass = getClassSql(langId, null, null);
        Map<String, String> resultClassMap = new HashMap();
        if (listClass != null && listClass.size() > 0) {
            for (MstComponentInspectClass mstComponentInspectClass : listClass) {
                String dictValue = mstComponentInspectClass.getMstComponentInspectLang() == null ? "" : mstComponentInspectClass.getMstComponentInspectLang().getDictValue();
                resultClassMap.put(mstComponentInspectClass.getDictKey().concat(STRING_FILE), dictValue);
            }
        }
        if (list != null && list.size() > 0) {
            Map<String, String> resultMap = new HashMap();
            for (MstComponentInspectType mstComponentInspectType : list) {
                mstComponentInspectTypeVo = new MstComponentInspectTypeVo();
                String key = mstComponentInspectType.getId().concat(STRING_FILE);
                if (resultMap.containsKey(key)) {
                    continue;
                }
                if (getMstComponetTypeIdExist(mstComponentInspectType.getId())) {
                    mstComponentInspectTypeVo.setDeleteFlag("0");
                } else {
                    mstComponentInspectTypeVo.setDeleteFlag("1");
                }
                mstComponentInspectTypeVo.setId(mstComponentInspectType.getId());
                mstComponentInspectTypeVo.setDictKey(mstComponentInspectType.getDictKey());
                if (mstComponentInspectType.getMstComponentInspectLang() != null) {
                    mstComponentInspectTypeVo.setDictValue(mstComponentInspectType.getMstComponentInspectLang().getDictValue());
                } else {
                    mstComponentInspectTypeVo.setDictValue("");
                }
                Iterator<MstComponentInspectFile> mstComponentInspectFileIterator = mstComponentInspectType.getMstComponentInspectFileCollection().iterator();
                List<MstComponentInspectFileVo> mstComponentInspectFileVos = new ArrayList();
                while (mstComponentInspectFileIterator.hasNext()) {
                    MstComponentInspectFileVo fileVo = new MstComponentInspectFileVo();
                    MstComponentInspectFile mstComponentInspectFile = mstComponentInspectFileIterator.next();
                    fileVo.setInspectClassId(mstComponentInspectFile.getMstComponentInspectFilePK().getInspectClassId());
                    fileVo.setInspectTypeId(mstComponentInspectFile.getMstComponentInspectFilePK().getInspectTypeId());
                    fileVo.setDrawingFlg(mstComponentInspectFile.getDrawingFlg());
                    fileVo.setProofFlg(mstComponentInspectFile.getProofFlg());
                    fileVo.setRohsProofFlg(mstComponentInspectFile.getRohsProofFlg());
                    fileVo.setPackageSpecFlg(mstComponentInspectFile.getPackageSpecFlg());
                    fileVo.setQcPhaseFlg(mstComponentInspectFile.getQcPhaseFlg());
                    fileVo.setFile06Flg(mstComponentInspectFile.getFile06Flg());
                    fileVo.setFile07Flg(mstComponentInspectFile.getFile07Flg());
                    fileVo.setFile08Flg(mstComponentInspectFile.getFile08Flg());
                    fileVo.setFile09Flg(mstComponentInspectFile.getFile09Flg());
                    fileVo.setFile10Flg(mstComponentInspectFile.getFile10Flg());
                    fileVo.setFile11Flg(mstComponentInspectFile.getFile11Flg());
                    fileVo.setFile12Flg(mstComponentInspectFile.getFile12Flg());
                    fileVo.setFile13Flg(mstComponentInspectFile.getFile13Flg());
                    fileVo.setFile14Flg(mstComponentInspectFile.getFile14Flg());
                    fileVo.setFile15Flg(mstComponentInspectFile.getFile15Flg());
                    fileVo.setFile16Flg(mstComponentInspectFile.getFile16Flg());
                    fileVo.setFile17Flg(mstComponentInspectFile.getFile17Flg());
                    fileVo.setFile18Flg(mstComponentInspectFile.getFile18Flg());
                    fileVo.setFile19Flg(mstComponentInspectFile.getFile19Flg());
                    fileVo.setFile20Flg(mstComponentInspectFile.getFile20Flg());

                    if (mstComponentInspectFile.getMstComponentInspectClass() != null) {
                        fileVo.setSeq(mstComponentInspectFile.getMstComponentInspectClass().getSeq());
                    }

                    if (mstComponentInspectFile.getMstComponentInspectClass() != null
                            && resultClassMap.containsKey(mstComponentInspectFile.getMstComponentInspectClass().getDictKey().concat(STRING_FILE))) {
                        fileVo.setDictValue(resultClassMap.get(mstComponentInspectFile.getMstComponentInspectClass().getDictKey().concat(STRING_FILE)));
                    } else {
                        fileVo.setDictValue("");
                    }

                    mstComponentInspectFileVos.add(fileVo);
                }

                if (mstComponentInspectFileVos.size() > 1) {
                    //SEQ昇順
                    Collections.sort(mstComponentInspectFileVos, new Comparator() {
                        @Override
                        public int compare(Object o1, Object o2) {
                            MstComponentInspectFileVo vo1 = (MstComponentInspectFileVo) o1;
                            MstComponentInspectFileVo vo2 = (MstComponentInspectFileVo) o2;

                            // 比較用のSEQ
                            return vo1.getSeq().compareTo(vo2.getSeq());
                        }
                    });
                }
                resultMap.put(key, key);
                mstComponentInspectTypeVo.setMstComponentInspectFileVos(mstComponentInspectFileVos);
                mstComponentInspectTypeVos.add(mstComponentInspectTypeVo);
            }
        }
        mstComponentInspectFileVoList.setMstComponentInspectTypeVos(mstComponentInspectTypeVos);

        return mstComponentInspectFileVoList;
    }

    /**
     * 部品業種取得
     *
     * @param langId
     * @return
     */
    public MstComponentInspectTypeList getMstComponentInspectTypelist(String langId) {
        MstComponentInspectTypeList mstComponentInspectTypeList = new MstComponentInspectTypeList();
        mstComponentInspectTypeList.setMstComponentInspectTypeVos(getMstComponentInspectTypeVolist(langId));
        return mstComponentInspectTypeList;
    }

    /**
     * 部品業種set value
     *
     * @param langId
     * @return
     */
    public List<MstComponentInspectTypeVo> getMstComponentInspectTypeVolist(String langId) {
        //部品業種
        List<MstComponentInspectTypeVo> mstComponentInspectTypeVos = new ArrayList();
        List<MstComponentInspectType> list = getTypeSql(langId, null, null, null, null);
        MstComponentInspectTypeVo typeVo;
        if (list != null && list.size() > 0) {
            for (MstComponentInspectType mstComponentInspectType : list) {
                typeVo = new MstComponentInspectTypeVo();
                if (getMstComponetTypeIdExist(mstComponentInspectType.getId())) {
                    typeVo.setDeleteFlag("0");
                } else {
                    typeVo.setDeleteFlag("1");
                }
                typeVo.setId(mstComponentInspectType.getId());
                typeVo.setDictKey(mstComponentInspectType.getDictKey());
                if (mstComponentInspectType.getMstComponentInspectLang() != null) {
                    typeVo.setDictValue(mstComponentInspectType.getMstComponentInspectLang().getDictValue());
                } else {
                    typeVo.setDictValue("");
                }
                mstComponentInspectTypeVos.add(typeVo);
            }
        }
        return mstComponentInspectTypeVos;
    }

    /**
     * 検査区分編集取得
     *
     * @param langId
     * @return
     */
    public MstComponentInspectClassList getMstComponentInspectClassList(String langId) {

        MstComponentInspectClassList mstComponentInspectClassList = new MstComponentInspectClassList();
        List<MstComponentInspectClassVo> mstComponentInspectClassVos = new ArrayList();
        List<MstComponentInspectClass> list = getClassSql(langId, null, null);
        MstComponentInspectClassVo classVo;
        
        Map<String, String> choiceMap = mstChoiceService.getChoiceMap(langId, new String[]{"mst_component_inspect_class.mass_flg"});
        
        if (list != null && list.size() > 0) {
            for (MstComponentInspectClass mstComponentInspectClass : list) {
                classVo = new MstComponentInspectClassVo();
                if (isInspectClassTypeDeletable(mstComponentInspectClass.getPk().getId())) {
                    classVo.setDeleteFlag("1");
                } else {
                    classVo.setDeleteFlag("0");
                }
                classVo.setId(mstComponentInspectClass.getPk().getId());
                classVo.setSeq(mstComponentInspectClass.getSeq());
                classVo.setDictKey(mstComponentInspectClass.getDictKey());
                classVo.setMassFlg(mstComponentInspectClass.getMassFlg());
                if(choiceMap != null) {
                    classVo.setMassFlgName(choiceMap.get("mst_component_inspect_class.mass_flg" + mstComponentInspectClass.getMassFlg()));
                } else {
                    classVo.setMassFlgName("");
                }
                
                if (mstComponentInspectClass.getMstComponentInspectLang() != null) {
                    classVo.setDictValue(mstComponentInspectClass.getMstComponentInspectLang().getDictValue());
                } else {
                    classVo.setDictValue("");
                }
                mstComponentInspectClassVos.add(classVo);
            }
        }
        mstComponentInspectClassList.setMstComponentInspectClassVos(mstComponentInspectClassVos);

        return mstComponentInspectClassList;
    }

    /**
     * 検査管理項目に対する検査区分及び、検査区分毎のエビデンスファイル取得
     *
     * @param componentId
     * @param componentCode
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @param loginUser
     * @return
     */
    public MstComponentInspectItemsTableClassList getMstComponentInspectionItemsTableClassList(String componentId,
            String componentCode, String outgoingCompanyId, String incomingCompanyId, LoginUser loginUser) {

        MstComponentInspectItemsTableClassList mstComponentInspectItemsTableClassList = new MstComponentInspectItemsTableClassList();
        List<MstComponentInspectionItemsTableClassVo> classes = findByOwner(incomingCompanyId, loginUser.getLangId()).stream().map(inspCls -> {
            MstComponentInspectionItemsTableClassVo ret = new MstComponentInspectionItemsTableClassVo();
            ret.setId(inspCls.getPk().getId());
            ret.setDictKey(inspCls.getDictKey());
            ret.setDictValue(inspCls.getMstComponentInspectLang().getDictValue());
            ret.setMassFlg(inspCls.getMassFlg());
            return ret;
        }).collect(Collectors.toList());
        mstComponentInspectItemsTableClassList.setMstComponentInspectionItemsTableClassVos(classes);

        return mstComponentInspectItemsTableClassList;
    }

    /**
     * 検査区分編集保存
     *
     * @param mstComponentInspectClassList
     * @param loginUser
     * @return
     */
    @Transactional
    public MstComponentInspectClassList saveMstComponentInspectClass(MstComponentInspectClassList mstComponentInspectClassList, LoginUser loginUser) {
        if (mstComponentInspectClassList != null && mstComponentInspectClassList.getMstComponentInspectClassVos() != null
                && mstComponentInspectClassList.getMstComponentInspectClassVos().size() > 0) {
            for (MstComponentInspectClassVo vo : mstComponentInspectClassList.getMstComponentInspectClassVos()) {
                if (StringUtils.isNotEmpty(vo.getOperationFlag())) {
                    switch (vo.getOperationFlag()) {
                        case CommonConstants.OPERATION_FLAG_DELETE: // delete
                            deleteMstComponentInspectClass(vo, loginUser);
                            break;
                        case CommonConstants.OPERATION_FLAG_UPDATE: // update
                            if (!getMstComponentInspectLangDictValueClassExist(vo.getDictValue(), loginUser.getLangId(), vo.getId())) {
                                updateMstComponentInspectClass(vo, loginUser);
                            } else {
                                mstComponentInspectClassList.setError(true);
                                mstComponentInspectClassList.setErrorCode(ErrorMessages.E201_APPLICATION);
                                mstComponentInspectClassList.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
                                return mstComponentInspectClassList;
                            }
                            break;
                        case CommonConstants.OPERATION_FLAG_CREATE: // add
                            MstCompany selfc = mstCompanyService.getSelfCompany();
                            if (getMstComponentInspectLangDictValueClassExist(vo.getDictValue(), loginUser.getLangId(), null)) {
                                mstComponentInspectClassList.setError(true);
                                mstComponentInspectClassList.setErrorCode(ErrorMessages.E201_APPLICATION);
                                mstComponentInspectClassList.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
                                return mstComponentInspectClassList;
                            } else if(selfc == null) {
                                mstComponentInspectClassList.setError(true);
                                mstComponentInspectClassList.setErrorCode(ErrorMessages.E201_APPLICATION);
                                mstComponentInspectClassList.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_self_company_nessesary"));
                                return mstComponentInspectClassList;
                            } {
                                createMstComponentInspectClass(vo, loginUser);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return mstComponentInspectClassList;
    }

    /**
     * 編集した検査ファイル設定を登録します
     *
     * @param mstComponentInspectFileList
     * @param loginUser
     * @return
     */
    @Transactional
    public MstComponentInspectFileList enterMstComponentInspectFile(MstComponentInspectFileList mstComponentInspectFileList, LoginUser loginUser) {
        if (mstComponentInspectFileList != null && mstComponentInspectFileList.getMstComponentInspectTypeVos() != null
                && mstComponentInspectFileList.getMstComponentInspectTypeVos().size() > 0) {
            //部品業種
            for (MstComponentInspectTypeVo vo : mstComponentInspectFileList.getMstComponentInspectTypeVos()) {
                if (StringUtils.isNotEmpty(vo.getOperationFlag())) {
                    switch (vo.getOperationFlag()) {
                        case CommonConstants.OPERATION_FLAG_DELETE: // delete
                            deleteMstComponentInspectType(vo, loginUser);
                            break;
                        case CommonConstants.OPERATION_FLAG_UPDATE: // update
                            if (getMstComponentInspectLangDictValueTypeExist(vo.getId(), vo.getDictValue(), loginUser.getLangId())) {
                                mstComponentInspectFileList.setError(true);
                                mstComponentInspectFileList.setErrorCode(ErrorMessages.E201_APPLICATION);
                                mstComponentInspectFileList.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
                                return mstComponentInspectFileList;
                            } else {
                                updateMstComponentInspectType(vo, loginUser);
                            }
                            break;
                        case CommonConstants.OPERATION_FLAG_CREATE: // add
                            if (getMstComponentInspectLangDictValueTypeExist(null, vo.getDictValue(), loginUser.getLangId())) {
                                mstComponentInspectFileList.setError(true);
                                mstComponentInspectFileList.setErrorCode(ErrorMessages.E201_APPLICATION);
                                mstComponentInspectFileList.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_error_record_exists"));
                                return mstComponentInspectFileList;
                            } else {
                                createMstComponentInspectType(vo, loginUser);
                            }
                            break;
                        default:
                            break;
                    }
                }
            }
        }
        return mstComponentInspectFileList;
    }

    /**
     * 検査区分編集，文言追加
     *
     * @param mstComponentInspectClassVo
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse createMstComponentInspectClass(MstComponentInspectClassVo mstComponentInspectClassVo, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        Date sysDate = new Date();
        MstComponentInspectClass mstComponentInspectClass;
        MstComponentInspectLang mstComponentInspectLang;
        MstComponentInspectLangPK mstComponentInspectLangPK;

        String uuid = IDGenerator.generate();
        String dictKey = INSPECT_TYPE.concat(uuid);
        //検査区分編集追加
        if (mstComponentInspectClassVo != null) {
            mstComponentInspectClass = new MstComponentInspectClass();
            mstComponentInspectClass.getPk().setId(uuid);
            mstComponentInspectClass.setMassFlg(mstComponentInspectClassVo.getMassFlg());
            mstComponentInspectClass.setSeq(mstComponentInspectClassVo.getSeq());
            mstComponentInspectClass.setDictKey(dictKey);
            mstComponentInspectClass.setCreateDate(sysDate);
            mstComponentInspectClass.setCreateUserUuid(loginUser.getUserUuid());
            mstComponentInspectClass.setUpdateDate(sysDate);
            mstComponentInspectClass.setUpdateUserUuid(loginUser.getUserUuid());

            entityManager.persist(mstComponentInspectClass);

            //文言追加
            for (String langId : LANG_ID.split(",")) {
                mstComponentInspectLang = new MstComponentInspectLang();
                mstComponentInspectLangPK = new MstComponentInspectLangPK();
                mstComponentInspectLangPK.setLangId(langId);
                mstComponentInspectLangPK.setDictKey(dictKey);
                mstComponentInspectLang.setMstComponentInspectLangPK(mstComponentInspectLangPK);
                if (StringUtils.isNotEmpty(mstComponentInspectClassVo.getDictValue())) {
                    mstComponentInspectLang.setDictValue(mstComponentInspectClassVo.getDictValue().trim());
                } else {
                    basicResponse.setError(true);
                    basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                    basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_deleted"));
                    return basicResponse;
                }
                mstComponentInspectLang.setCreateDate(sysDate);
                mstComponentInspectLang.setCreateUserUuid(loginUser.getUserUuid());
                mstComponentInspectLang.setUpdateDate(sysDate);
                mstComponentInspectLang.setUpdateUserUuid(loginUser.getUserUuid());
                entityManager.persist(mstComponentInspectLang);
            }

            List<MstComponentInspectType> list = getTypeSql(loginUser.getLangId(), null, null, null, null);
            if (list != null && list.size() > 0) {
                for (MstComponentInspectType type : list) {
                    createMstComponentInspectFile(type.getId(), uuid, null, loginUser);
                }
            }

        }
        return basicResponse;
    }

    /**
     * 検査区分編集,文言更新
     *
     * @param mstComponentInspectClassVo
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse updateMstComponentInspectClass(MstComponentInspectClassVo mstComponentInspectClassVo, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        Date sysDate = new Date();
        MstComponentInspectClass mstComponentInspectClass;
        MstComponentInspectLang mstComponentInspectLang;

        if (mstComponentInspectClassVo != null) {
            List<MstComponentInspectClass> classList = getClassSql(loginUser.getLangId(), mstComponentInspectClassVo.getId(), null);
            if (classList != null && classList.size() > 0) {
                //検査区分編集更新
                mstComponentInspectClass = classList.get(0);
                mstComponentInspectClass.getPk().setId(mstComponentInspectClassVo.getId());
                mstComponentInspectClass.setSeq(mstComponentInspectClassVo.getSeq());
                mstComponentInspectClass.setMassFlg(mstComponentInspectClassVo.getMassFlg());
                mstComponentInspectClass.setUpdateDate(sysDate);
                mstComponentInspectClass.setUpdateUserUuid(loginUser.getUserUuid());

                entityManager.merge(mstComponentInspectClass);
                updBatUpdStatus(mstComponentInspectClass.getPk().getId(), BatUpdStatus.NotPushed);

                //文言更新
                mstComponentInspectLang = getLangSql(loginUser.getLangId(), mstComponentInspectClassVo.getDictKey());

                if (mstComponentInspectLang != null) {
                    mstComponentInspectLang.setDictValue(mstComponentInspectClassVo.getDictValue().trim());
                } else {
                    basicResponse.setError(true);
                    basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                    basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_deleted"));
                    return basicResponse;
                }
                mstComponentInspectLang.setUpdateDate(sysDate);
                mstComponentInspectLang.setUpdateUserUuid(loginUser.getUserUuid());

                entityManager.merge(mstComponentInspectLang);
            }
        }
        return basicResponse;
    }

    /**
     * 検査区分編集删除
     *
     * @param mstComponentInspectClassVo
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse deleteMstComponentInspectClass(MstComponentInspectClassVo mstComponentInspectClassVo, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        Query query = entityManager.createNamedQuery("MstComponentInspectClass.deleteByClassId");
        query.setParameter("classId", mstComponentInspectClassVo.getId());
        int count = query.executeUpdate();
        if (count > 0) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_deleted"));

            //文言删除
            deleteMstComponentInspectLang(mstComponentInspectClassVo.getDictKey(), loginUser.getLangId());

            //部品業種、検査区分毎の添付ファイル設定删除
            Query queryFile = entityManager.createNamedQuery("MstComponentInspectFile.deleteByInspectClassId");
            queryFile.setParameter("inspectClassId", mstComponentInspectClassVo.getId());
            queryFile.executeUpdate();
        }
        return basicResponse;
    }

    /**
     * 部品業種，文言追加
     *
     * @param mstComponentInspectTypeVo
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse createMstComponentInspectType(MstComponentInspectTypeVo mstComponentInspectTypeVo, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        Date sysDate = new Date();
        MstComponentInspectType mstComponentInspectType;
        MstComponentInspectLang mstComponentInspectLang;
        MstComponentInspectLangPK mstComponentInspectLangPK;
        Map<String, MstComponentInspectFileVo> resultMap = new HashMap();

        String uuid = IDGenerator.generate();
        String dictKey = INSPECT_TYPE.concat(uuid);
        //部品業種追加
        if (mstComponentInspectTypeVo != null) {
            mstComponentInspectType = new MstComponentInspectType();
            mstComponentInspectType.setId(uuid);
            mstComponentInspectType.setDictKey(dictKey);
            mstComponentInspectType.setCreateDate(sysDate);
            mstComponentInspectType.setCreateUserUuid(loginUser.getUserUuid());
            mstComponentInspectType.setUpdateDate(sysDate);
            mstComponentInspectType.setUpdateUserUuid(loginUser.getUserUuid());
            mstComponentInspectType.setMstComponentInspectFileCollection(null);
            mstComponentInspectType.setOwnerCompanyId(null);
            entityManager.persist(mstComponentInspectType);

            //文言追加
            for (String langId : LANG_ID.split(",")) {
                mstComponentInspectLang = new MstComponentInspectLang();
                mstComponentInspectLangPK = new MstComponentInspectLangPK();
                mstComponentInspectLangPK.setLangId(langId);
                mstComponentInspectLangPK.setDictKey(dictKey);
                mstComponentInspectLang.setMstComponentInspectLangPK(mstComponentInspectLangPK);
                if (StringUtils.isNotEmpty(mstComponentInspectTypeVo.getDictValue())) {
                    mstComponentInspectLang.setDictValue(mstComponentInspectTypeVo.getDictValue().trim());
                } else {
                    basicResponse.setError(true);
                    basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                    basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_deleted"));
                    return basicResponse;
                }
                mstComponentInspectLang.setCreateDate(sysDate);
                mstComponentInspectLang.setCreateUserUuid(loginUser.getUserUuid());
                mstComponentInspectLang.setUpdateDate(sysDate);
                mstComponentInspectLang.setUpdateUserUuid(loginUser.getUserUuid());
                entityManager.persist(mstComponentInspectLang);
            }

            //検査ファイル種類設定テーブル追加
            if (mstComponentInspectTypeVo.getMstComponentInspectFileVos() != null && mstComponentInspectTypeVo.getMstComponentInspectFileVos().size() > 0) {
                for (MstComponentInspectFileVo vo : mstComponentInspectTypeVo.getMstComponentInspectFileVos()) {
                    resultMap.put(vo.getInspectClassId().concat(STRING_FILE), vo);
                }
            }
            List<MstComponentInspectClass> list = getClassSql(loginUser.getLangId(), null, null);
            if (list != null && list.size() > 0) {
                String key;
                for (MstComponentInspectClass mstComponentInspectClass : list) {
                    key = mstComponentInspectClass.getPk().getId().concat(STRING_FILE);
                    //部品業種、検査区分毎の添付ファイル設定追加
                    if (resultMap.containsKey(key)) {
                        createMstComponentInspectFile(uuid, mstComponentInspectClass.getPk().getId(), resultMap.get(key), loginUser);
                    } else {
                        createMstComponentInspectFile(uuid, mstComponentInspectClass.getPk().getId(), null, loginUser);
                    }
                }
            }
        }
        return basicResponse;
    }

    /**
     * 部品業種，文言更新
     *
     * @param mstComponentInspectTypeVo
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse updateMstComponentInspectType(MstComponentInspectTypeVo mstComponentInspectTypeVo, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        Date sysDate = new Date();

        if (mstComponentInspectTypeVo != null) {
            //文言更新
            MstComponentInspectLang mstComponentInspectLang = getLangSql(loginUser.getLangId(), mstComponentInspectTypeVo.getDictKey());

            if (mstComponentInspectLang != null) {
                mstComponentInspectLang.setDictValue(mstComponentInspectTypeVo.getDictValue().trim());
            } else {
                basicResponse.setError(true);
                basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
                basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_deleted"));
                return basicResponse;
            }
            mstComponentInspectLang.setUpdateDate(sysDate);
            mstComponentInspectLang.setUpdateUserUuid(loginUser.getUserUuid());

            entityManager.merge(mstComponentInspectLang);
            updBatUpdStatus(mstComponentInspectTypeVo.getId(), BatUpdStatus.NotPushed);
            
            //検査ファイル種類設定テーブル更新
            controlMstComponentInspectFile(mstComponentInspectTypeVo.getMstComponentInspectFileVos(), loginUser);
        }
        return basicResponse;
    }
    
    private void updBatUpdStatus(String id, BatUpdStatus status) {
        List<MstInspectExtBatchStatus> batStatuses = entityManager.createNamedQuery("MstInspectExtBatchStatus.findById", MstInspectExtBatchStatus.class)
            .setParameter("id", id).getResultList();
        batStatuses.forEach(b ->{
            b.setBatUpdStatus(status);
            entityManager.persist(b);
        });
    }

    /**
     * 部品業種删除
     *
     * @param mstComponentInspectTypeVo
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse deleteMstComponentInspectType(MstComponentInspectTypeVo mstComponentInspectTypeVo, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        String typeId = mstComponentInspectTypeVo.getId();
        Query query = entityManager.createNamedQuery("MstComponentInspectType.deleteByTypeId");
        query.setParameter("typeId", typeId);
        int count = query.executeUpdate();
        if (count > 0) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_deleted"));

            deleteMstComponentInspectLang(mstComponentInspectTypeVo.getDictKey(), loginUser.getLangId());

            //部品業種、検査区分毎の添付ファイル設定删除
            Query queryFile = entityManager.createNamedQuery("MstComponentInspectFile.deleteByInspectTypeId");
            queryFile.setParameter("inspectTypeId", typeId);
            queryFile.executeUpdate();
        }
        return basicResponse;
    }

    /**
     * 検査ファイル種類設定テーブル
     *
     * @param mstComponentInspectFileVos
     * @param loginUser
     */
    @Transactional
    public void controlMstComponentInspectFile(List<MstComponentInspectFileVo> mstComponentInspectFileVos, LoginUser loginUser) {
        List<MstComponentInspectClass> list = getClassSql(loginUser.getLangId(), null, null);
        if (list != null && list.size() > 0) {
            Map<String, MstComponentInspectFileVo> resultMap = new HashMap();
            String key;
            for (MstComponentInspectFileVo vo : mstComponentInspectFileVos) {
                resultMap.put(vo.getInspectClassId().concat(STRING_FILE), vo);
            }
            for (MstComponentInspectClass mstComponentInspectClass : list) {
                //部品業種、検査区分毎の添付ファイル設定追加
                key = mstComponentInspectClass.getPk().getId().concat(STRING_FILE);
                if (!resultMap.containsKey(key)) {
                    List<MstComponentInspectType> listType = getTypeSql(loginUser.getLangId(), null, null, null, null);
                    if (listType != null) {
                        for (MstComponentInspectType mstComponentInspectType : listType) {
                            createMstComponentInspectFile(mstComponentInspectType.getId(), mstComponentInspectClass.getPk().getId(), null, loginUser);
                        }
                    }
                } else {
                    updateMstComponentInspectFile(resultMap.get(key), loginUser);
                }
            }
        }
    }

    /**
     * 検査ファイル種類設定テーブル追加
     *
     * @param typeId
     * @param classId
     * @param fileVo
     * @param loginUser
     */
    @Transactional
    public void createMstComponentInspectFile(String typeId, String classId, MstComponentInspectFileVo fileVo, LoginUser loginUser) {
        Date sysDate = new Date();
        String charFlag = "0";
        Character charFileFlag = '2';

        MstComponentInspectFile mstComponentInspectFile = new MstComponentInspectFile();
        MstComponentInspectFilePK mstComponentInspectFilePK = new MstComponentInspectFilePK();
        mstComponentInspectFilePK.setInspectTypeId(typeId);
        mstComponentInspectFilePK.setInspectClassId(classId);
        mstComponentInspectFile.setMstComponentInspectFilePK(mstComponentInspectFilePK);
        if (fileVo != null) {
            mstComponentInspectFile.setDrawingFlg(fileVo.getDrawingFlg());
            mstComponentInspectFile.setProofFlg(fileVo.getProofFlg());
            mstComponentInspectFile.setRohsProofFlg(fileVo.getRohsProofFlg());
            mstComponentInspectFile.setPackageSpecFlg(fileVo.getPackageSpecFlg());
            mstComponentInspectFile.setQcPhaseFlg(fileVo.getQcPhaseFlg());
            //update file 06->20 flg
            mstComponentInspectFile.setFile06Flg(fileVo.getFile06Flg());
            mstComponentInspectFile.setFile07Flg(fileVo.getFile07Flg());
            mstComponentInspectFile.setFile08Flg(fileVo.getFile08Flg());
            mstComponentInspectFile.setFile09Flg(fileVo.getFile09Flg());
            mstComponentInspectFile.setFile10Flg(fileVo.getFile10Flg());
            mstComponentInspectFile.setFile11Flg(fileVo.getFile11Flg());
            mstComponentInspectFile.setFile12Flg(fileVo.getFile12Flg());
            mstComponentInspectFile.setFile13Flg(fileVo.getFile13Flg());
            mstComponentInspectFile.setFile14Flg(fileVo.getFile14Flg());
            mstComponentInspectFile.setFile15Flg(fileVo.getFile15Flg());
            mstComponentInspectFile.setFile16Flg(fileVo.getFile16Flg());
            mstComponentInspectFile.setFile17Flg(fileVo.getFile17Flg());
            mstComponentInspectFile.setFile18Flg(fileVo.getFile18Flg());
            mstComponentInspectFile.setFile19Flg(fileVo.getFile19Flg());
            mstComponentInspectFile.setFile20Flg(fileVo.getFile20Flg());
        } else {
            mstComponentInspectFile.setDrawingFlg(charFlag.charAt(0));
            mstComponentInspectFile.setProofFlg(charFlag.charAt(0));
            mstComponentInspectFile.setRohsProofFlg(charFlag.charAt(0));
            mstComponentInspectFile.setPackageSpecFlg(charFlag.charAt(0));
            mstComponentInspectFile.setQcPhaseFlg(charFlag.charAt(0));
            //update default file 06->20 flg
            mstComponentInspectFile.setFile06Flg(charFileFlag);
            mstComponentInspectFile.setFile07Flg(charFileFlag);
            mstComponentInspectFile.setFile08Flg(charFileFlag);
            mstComponentInspectFile.setFile09Flg(charFileFlag);
            mstComponentInspectFile.setFile10Flg(charFileFlag);
            mstComponentInspectFile.setFile11Flg(charFileFlag);
            mstComponentInspectFile.setFile12Flg(charFileFlag);
            mstComponentInspectFile.setFile13Flg(charFileFlag);
            mstComponentInspectFile.setFile14Flg(charFileFlag);
            mstComponentInspectFile.setFile15Flg(charFileFlag);
            mstComponentInspectFile.setFile16Flg(charFileFlag);
            mstComponentInspectFile.setFile17Flg(charFileFlag);
            mstComponentInspectFile.setFile18Flg(charFileFlag);
            mstComponentInspectFile.setFile19Flg(charFileFlag);
            mstComponentInspectFile.setFile20Flg(charFileFlag);
        }
        mstComponentInspectFile.setCreateDate(sysDate);
        mstComponentInspectFile.setCreateUserUuid(loginUser.getUserUuid());
        mstComponentInspectFile.setUpdateDate(sysDate);
        mstComponentInspectFile.setUpdateUserUuid(loginUser.getUserUuid());

        entityManager.persist(mstComponentInspectFile);
    }

    /**
     * 検査ファイル種類設定テーブル更新
     *
     * @param fileVo
     * @param loginUser
     */
    @Transactional
    public void updateMstComponentInspectFile(MstComponentInspectFileVo fileVo, LoginUser loginUser) {
        Date sysDate = new Date();
        MstComponentInspectFile mstComponentInspectFile = new MstComponentInspectFile();
        MstComponentInspectFilePK mstComponentInspectFilePK = new MstComponentInspectFilePK();
        mstComponentInspectFilePK.setInspectTypeId(fileVo.getInspectTypeId());
        mstComponentInspectFilePK.setInspectClassId(fileVo.getInspectClassId());
        mstComponentInspectFile.setMstComponentInspectFilePK(mstComponentInspectFilePK);
        mstComponentInspectFile.setDrawingFlg(fileVo.getDrawingFlg());
        mstComponentInspectFile.setProofFlg(fileVo.getProofFlg());
        mstComponentInspectFile.setRohsProofFlg(fileVo.getRohsProofFlg());
        mstComponentInspectFile.setPackageSpecFlg(fileVo.getPackageSpecFlg());
        mstComponentInspectFile.setQcPhaseFlg(fileVo.getQcPhaseFlg());
        mstComponentInspectFile.setCreateDate(sysDate);
        mstComponentInspectFile.setCreateUserUuid(loginUser.getUserUuid());
        mstComponentInspectFile.setUpdateDate(sysDate);
        mstComponentInspectFile.setUpdateUserUuid(loginUser.getUserUuid());
        //update file flag from 06->20
        mstComponentInspectFile.setFile06Flg(fileVo.getFile06Flg());
        mstComponentInspectFile.setFile07Flg(fileVo.getFile07Flg());
        mstComponentInspectFile.setFile08Flg(fileVo.getFile08Flg());
        mstComponentInspectFile.setFile09Flg(fileVo.getFile09Flg());
        mstComponentInspectFile.setFile10Flg(fileVo.getFile10Flg());
        mstComponentInspectFile.setFile11Flg(fileVo.getFile11Flg());
        mstComponentInspectFile.setFile12Flg(fileVo.getFile12Flg());
        mstComponentInspectFile.setFile13Flg(fileVo.getFile13Flg());
        mstComponentInspectFile.setFile14Flg(fileVo.getFile14Flg());
        mstComponentInspectFile.setFile15Flg(fileVo.getFile15Flg());
        mstComponentInspectFile.setFile16Flg(fileVo.getFile16Flg());
        mstComponentInspectFile.setFile17Flg(fileVo.getFile17Flg());
        mstComponentInspectFile.setFile18Flg(fileVo.getFile18Flg());
        mstComponentInspectFile.setFile19Flg(fileVo.getFile19Flg());
        mstComponentInspectFile.setFile20Flg(fileVo.getFile20Flg());
        entityManager.merge(mstComponentInspectFile);
    }

    /**
     * 検査ファイル種類設定テーブル删除
     *
     * @param inspectTypeId
     * @param inspectClassId
     * @param loginUser
     * @return
     */
    @Transactional
    public BasicResponse deleteMstComponentInspectFile(String inspectTypeId, String inspectClassId, LoginUser loginUser) {
        BasicResponse basicResponse = new BasicResponse();
        Query query = entityManager.createQuery("DELETE FROM MstComponentInspectFile m"
                + " WHERE m.mstComponentInspectFilePK.inspectTypeId = :inspectTypeId"
                + " AND m.mstComponentInspectFilePK.inspectClassId = :inspectClassId")
                .setParameter("inspectTypeId", inspectTypeId)
                .setParameter("inspectClassId", inspectClassId);
        int count = query.executeUpdate();
        if (count > 0) {
            basicResponse.setError(true);
            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "msg_record_deleted"));
        }
        return basicResponse;
    }

    /**
     * 部品業種SQL
     *
     * @param langId
     * @param dictKey
     * @param dictValue
     * @param typeId
     * @param companyId
     * @return
     */
    public List<MstComponentInspectType> getTypeSql(String langId, String dictKey, String dictValue, String typeId, String companyId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT m FROM MstComponentInspectType m");
        queryBuilder.append(" JOIN FETCH m.mstComponentInspectLang mstComponentInspectLang");
        queryBuilder.append(" WHERE mstComponentInspectLang.mstComponentInspectLangPK.langId = :langId");
        if (StringUtils.isNotEmpty(typeId)) {
            queryBuilder.append(" AND m.id = :typeId");
        }
        if (StringUtils.isNotEmpty(dictKey)) {
            queryBuilder.append(" AND m.dictKey = :dictKey");
        }
        if (StringUtils.isNotEmpty(dictValue)) {
            queryBuilder.append(" AND mstComponentInspectLang.dictValue = :dictValue");
        }
        if (StringUtils.isNotEmpty(companyId)) {
            queryBuilder.append(" AND m.ownerCompanyId = :ownerCompanyId ");
        } else {
            queryBuilder.append(" AND m.ownerCompanyId is null ");
        }

        queryBuilder.append(" ORDER BY m.createDate ASC");

        TypedQuery<MstComponentInspectType> query = entityManager.createQuery(queryBuilder.toString(), MstComponentInspectType.class);

        query.setParameter("langId", langId);
        if (StringUtils.isNotEmpty(typeId)) {
            query.setParameter("typeId", typeId);
        }
        if (StringUtils.isNotEmpty(dictKey)) {
            query.setParameter("dictKey", dictKey);
        }
        if (StringUtils.isNotEmpty(dictValue)) {
            query.setParameter("dictValue", dictValue);
        }
        if (StringUtils.isNotEmpty(companyId)) {
            query.setParameter("ownerCompanyId", companyId);
        }
        List<MstComponentInspectType> list = query.getResultList();
        return list;
    }

    /**
     * 検査ファイル種類設定テーブルSQL
     *
     * @param langId
     * @return
     */
    public List getFileSql(String langId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT m FROM MstComponentInspectType m");
        queryBuilder.append(" LEFT JOIN FETCH m.mstComponentInspectFileCollection fileCollection");
        queryBuilder.append(" LEFT JOIN FETCH fileCollection.mstComponentInspectClass mstComponentInspectClass");
        queryBuilder.append(" LEFT JOIN FETCH m.mstComponentInspectLang mstComponentInspectLang");
        queryBuilder.append(" WHERE mstComponentInspectLang.mstComponentInspectLangPK.langId = :langId");
        queryBuilder.append(" AND (fileCollection.mstComponentInspectFilePK.ownerCompanyId = 'SELF' OR fileCollection.mstComponentInspectFilePK.ownerCompanyId IS NULL)");
        queryBuilder.append(" ORDER BY m.createDate ASC");
//        queryBuilder.append(" GROUP BY m.id");

        Query query = entityManager.createQuery(queryBuilder.toString()).setParameter("langId", langId);

        List list = query.getResultList();
        return list;
    }

    /**
     * 部品検査区分テーブルSQL
     *
     * @param langId
     * @param classId
     * @param dictValue
     * @return
     */
    public List getClassSql(String langId, String classId, String dictValue) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT m FROM MstComponentInspectClass m");
        queryBuilder.append(" JOIN FETCH m.mstComponentInspectLang mstComponentInspectLang");
        queryBuilder.append(" WHERE mstComponentInspectLang.mstComponentInspectLangPK.langId = :langId");
        queryBuilder.append(" AND m.pk.ownerCompanyId = 'SELF' ");
        if (StringUtils.isNotEmpty(classId)) {
            queryBuilder.append(" AND m.pk.id = :classId");
        }
        if (StringUtils.isNotEmpty(dictValue)) {
            queryBuilder.append(" AND mstComponentInspectLang.dictValue = :dictValue");
        }

        queryBuilder.append(" ORDER BY m.seq ASC");

        Query query = entityManager.createQuery(queryBuilder.toString());

        query.setParameter("langId", langId);

        if (StringUtils.isNotEmpty(classId)) {
            query.setParameter("classId", classId);
        }
        if (StringUtils.isNotEmpty(dictValue)) {
            query.setParameter("dictValue", dictValue);
        }
        List list = query.getResultList();
        return list;
    }

    /**
     * 検査の選択肢用文言。ユーザが追加編集可能なデータを多言語対応するためのテーブル取得
     *
     * @param langId
     * @param dictKey
     * @return
     */
    public MstComponentInspectLang getLangSql(String langId, String dictKey) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT m FROM MstComponentInspectLang m");
        queryBuilder.append(" WHERE m.mstComponentInspectLangPK.langId = :langId");
        queryBuilder.append(" AND m.mstComponentInspectLangPK.dictKey = :dictKey");

        Query query = entityManager.createQuery(queryBuilder.toString(), MstComponentInspectLang.class);

        query.setParameter("langId", langId);
        query.setParameter("dictKey", dictKey);

        return (MstComponentInspectLang) query.getSingleResult();
    }

    /**
     * 検査ファイル種類設定テーブル存在チェックを行う
     *
     * @param inspectTypeId
     * @param inspectClassId
     * @return
     */
    public List getFileByTypeClassId(String inspectTypeId, String inspectClassId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT m FROM MstComponentInspectFile m");
        queryBuilder.append(" LEFT JOIN FETCH m.mstComponentInspectClass");
        queryBuilder.append(" WHERE 1=1");
        if (StringUtils.isNotEmpty(inspectTypeId)) {
            queryBuilder.append(" AND m.mstComponentInspectFilePK.inspectTypeId = :inspectTypeId");
        }
        if (StringUtils.isNotEmpty(inspectClassId)) {
            queryBuilder.append(" AND m.mstComponentInspectFilePK.inspectClassId = :inspectClassId");
        }

        Query query = entityManager.createQuery(queryBuilder.toString(), MstComponentInspectFile.class);

        if (StringUtils.isNotEmpty(inspectTypeId)) {
            query.setParameter("inspectTypeId", inspectTypeId);
        }
        if (StringUtils.isNotEmpty(inspectClassId)) {
            query.setParameter("inspectClassId", inspectClassId);
        }

        try {
            return query.getResultList();
        } catch (NoResultException e) {
            return null;
        }
    }

    /**
     * 部品業種文言dictValue存在チェックを行う
     *
     * @param typeId
     * @param dictValue
     * @param langId
     * @return
     */
    public boolean getMstComponentInspectLangDictValueTypeExist(String typeId, String dictValue, String langId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT t FROM MstComponentInspectType t");
        queryBuilder.append(" JOIN FETCH t.mstComponentInspectLang mstComponentInspectLang");
        queryBuilder.append(" WHERE mstComponentInspectLang.mstComponentInspectLangPK.langId = :langId");
        queryBuilder.append(" AND mstComponentInspectLang.dictValue = :dictValue");
        queryBuilder.append(" AND t.ownerCompanyId is null");
        if (StringUtils.isNotEmpty(typeId)) {
            queryBuilder.append(" AND t.id <> :typeId");
        }
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("langId", langId);
        query.setParameter("dictValue", dictValue);
        if (StringUtils.isNotEmpty(typeId)) {
            query.setParameter("typeId", typeId);
        }
        try {
            List list = query.getResultList();
            if (list != null && list.size() > 0) {
                return true;
            }
        } catch (NoResultException e) {
            return false;
        }
        return false;
    }

    /**
     * 検査区分編集文言dictValue存在チェックを行う
     *
     * @param dictValue
     * @param langId
     * @param classId
     * @return
     */
    public boolean getMstComponentInspectLangDictValueClassExist(String dictValue, String langId, String classId) {
        StringBuilder queryBuilder = new StringBuilder();
        queryBuilder.append("SELECT t FROM MstComponentInspectClass t");
        queryBuilder.append(" JOIN FETCH t.mstComponentInspectLang mstComponentInspectLang");
        queryBuilder.append(" WHERE mstComponentInspectLang.mstComponentInspectLangPK.langId = :langId");
        queryBuilder.append(" AND mstComponentInspectLang.dictValue = :dictValue");
        queryBuilder.append(" AND t.pk.ownerCompanyId = 'SELF' ");
        if (StringUtils.isNotEmpty(classId)) {
            queryBuilder.append(" AND t.pk.id <> :classId");
        }
        Query query = entityManager.createQuery(queryBuilder.toString());
        query.setParameter("langId", langId);
        query.setParameter("dictValue", dictValue);
        if (StringUtils.isNotEmpty(classId)) {
            query.setParameter("classId", classId);
        }
        try {
            List list = query.getResultList();
            if (list != null && list.size() > 0) {
                return true;
            }
        } catch (NoResultException e) {
            return false;
        }
        return false;
    }

    /**
     * テーブル情報inspectTypeId存在チェックを行う
     *
     * @param inspectTypeId
     * @return
     */
    public boolean getMstComponetTypeIdExist(String inspectTypeId) {
        Query query = entityManager.createNamedQuery("MstComponentInspectionItemsTable.findByinspectTypeId");
        query.setParameter("inspectTypeId", inspectTypeId);
        try {
            List list = query.getResultList();
            if (list != null && list.size() > 0) {
                return true;
            }
        } catch (NoResultException e) {
            return false;
        }
        return false;
    }

    /**
     * 文言删除
     *
     * @param dictKey
     * @param langId
     * @return
     */
    @Transactional
    public BasicResponse deleteMstComponentInspectLang(String dictKey, String langId) {
        BasicResponse basicResponse = new BasicResponse();
//        Query query = entityManager.createNamedQuery("MstComponentInspectLang.findByDictKey");
//        query.setParameter("dictKey", dictKey);
//        List list = query.getResultList();
//        if(list != null && list.size() > 0) {
//            basicResponse.setError(true);
//            basicResponse.setErrorCode(ErrorMessages.E201_APPLICATION);
//            basicResponse.setErrorMessage(mstDictionaryService.getDictionaryValue(loginUser.getLangId(), "mst_error_record_not_found)"));
//            return basicResponse;
//        }
        Query query = entityManager.createNamedQuery("MstComponentInspectLang.deleteByDictKey");
        query.setParameter("dictKey", dictKey);
        query.executeUpdate();
        return basicResponse;
    }

    /**
     * 検査管理項目に対する検査区分及び、検査区分毎のエビデンスファイル登録取得
     *
     * @param componentId
     * @param componentCode
     * @param outgoingCompanyId
     * @param incomingCompanyId
     * @param loginUser
     * @param dictValue
     * @return
     */
    public List getMstComponentInspectionItemsTableClass(String componentId, String componentCode, String outgoingCompanyId, String incomingCompanyId, LoginUser loginUser, String dictValue) {
        StringBuilder queryBuilder = new StringBuilder();
        MstCompany selfCompany = this.mstCompanyService.getSelfCompany();
//        queryBuilder.append("SELECT m FROM MstComponentInspectionItemsTableClass m");
//        queryBuilder.append(" JOIN FETCH m.mstComponentInspectLang mstComponentInspectLang");
//        queryBuilder.append(" WHERE mstComponentInspectLang.mstComponentInspectLangPK.langId = :langId");
//        queryBuilder.append(" AND m.componentInspectionItemsTableId = ");
//        if(StringUtils.isNotEmpty(componentCode)) {
//            queryBuilder.append(" (SELECT t.id FROM MstComponentInspectionItemsTable t");
//            queryBuilder.append(" JOIN FETCH t.mstComponent mstComponent");
//            queryBuilder.append(" WHERE mstComponet.componentCode = :componentCode ");
//            queryBuilder.append(" AND t.outgoingCompanyId = :outgoingCompanyId ");
//            queryBuilder.append(" AND t.incomingCompanyId = :incomingCompanyId ");
//            queryBuilder.append(" ORDER BY t.version DESC limit 1) ");
//        } else {
//            queryBuilder.append(" (SELECT t.id FROM MstComponentInspectionItemsTable t ");
//            queryBuilder.append(" WHERE t.componentId = :componentId ");
//            queryBuilder.append(" AND t.outgoingCompanyId = :outgoingCompanyId ");
//            queryBuilder.append(" AND t.incomingCompanyId = :incomingCompanyId ");
//            queryBuilder.append(" ORDER BY t.version DESC limit 1) ");
//        }
//        if (StringUtils.isNotEmpty(dictValue)) {
//            queryBuilder.append(" AND mstComponentInspectLang.dictValue = :dictValue");
//        }
//        queryBuilder.append(" GROUP BY m.dictKey");
//        
//        Query query = entityManager.createQuery(queryBuilder.toString());
//
//        query.setParameter("langId", loginUser.getLangId());
//        if (StringUtils.isNotEmpty(componentCode)) {
//            query.setParameter("componentCode", componentCode);
//        } else {
//            query.setParameter("componentId", componentId);
//        }
//        if(StringUtils.isNotEmpty(outgoingCompanyId)) {
//            query.setParameter("outgoingCompanyId", outgoingCompanyId);
//        } else {
//            query.setParameter("outgoingCompanyId", selfCompany.getId());
//        }
//        if(StringUtils.isNotEmpty(incomingCompanyId)) {
//            query.setParameter("incomingCompanyId", incomingCompanyId);
//        } else {
//            query.setParameter("incomingCompanyId", selfCompany.getId());
//        }
//        if (StringUtils.isNotEmpty(dictValue)) {
//            query.setParameter("dictValue", dictValue);
//        }
        int index = 1;
        queryBuilder.append("SELECT m.COMPONENT_INSPECT_CLASS_ID,m.DICT_KEY,mstComponentInspectLang.DICT_VALUE,m.DRAWING_FLG,m.PROOF_FLG,m.ROHS_PROOF_FLG,");
        queryBuilder.append("  m.PACKAGE_SPEC_FLG,m.QC_PHASE_FLG,m.MASS_FLG FROM mst_component_inspection_items_table_class m ");
        queryBuilder.append(" JOIN mst_component_inspect_lang mstComponentInspectLang ON m.DICT_KEY = mstComponentInspectLang.DICT_KEY ");
        queryBuilder.append(" WHERE mstComponentInspectLang.LANG_ID = ?");
        queryBuilder.append(" AND m.COMPONENT_INSPECTION_ITEMS_TABLE_ID = ");
        if (StringUtils.isNotEmpty(componentCode)) {
            queryBuilder.append(" (SELECT t.id FROM mst_component_inspection_items_table t LEFT JOIN mst_component mstComponet ON mstComponet.ID = t.COMPONENT_ID");
            queryBuilder.append(" WHERE mstComponet.COMPONENT_CODE = ?");
            if (StringUtils.isNotEmpty(outgoingCompanyId)) {
                queryBuilder.append(" AND t.OUTGOING_COMPANY_ID = ? AND t.INCOMING_COMPANY_ID = ?");
            }
            if (StringUtils.isNotEmpty(incomingCompanyId)) {
                queryBuilder.append(" AND t.OUTGOING_COMPANY_ID = ? AND t.INCOMING_COMPANY_ID = ?");
            }
            queryBuilder.append(" ORDER BY t.version DESC LIMIT 1) ");
        } else {
            queryBuilder.append(" (SELECT t.id FROM mst_component_inspection_items_table t WHERE 1 = 1");
            if(StringUtils.isNotEmpty(componentId)) {
                queryBuilder.append(" AND t.COMPONENT_ID = ?");
            }
            if (StringUtils.isNotEmpty(outgoingCompanyId)) {
                queryBuilder.append(" AND t.OUTGOING_COMPANY_ID = ? AND t.INCOMING_COMPANY_ID = ?");
            }
            if (StringUtils.isNotEmpty(incomingCompanyId)) {
                queryBuilder.append(" AND t.OUTGOING_COMPANY_ID = ? AND t.INCOMING_COMPANY_ID = ?");
            }
            queryBuilder.append(" ORDER BY t.version DESC LIMIT 1) ");
        }
        if (StringUtils.isNotEmpty(dictValue)) {
            queryBuilder.append(" AND mstComponentInspectLang.DICT_VALUE = ?");
        }
        queryBuilder.append(" GROUP BY m.DICT_KEY");
        queryBuilder.append(" ORDER BY m.SEQ");

        Query query = entityManager.createNativeQuery(queryBuilder.toString());

        query.setParameter(index++, loginUser.getLangId());
        if (StringUtils.isNotEmpty(componentCode)) {
            query.setParameter(index++, componentCode);
        }
        if(StringUtils.isNotEmpty(componentId)) {
            query.setParameter(index++, componentId);
        }
        if (StringUtils.isNotEmpty(outgoingCompanyId)) {
            query.setParameter(index++, outgoingCompanyId);
            query.setParameter(index++, selfCompany.getId());
        }
        if (StringUtils.isNotEmpty(incomingCompanyId)) {
            query.setParameter(index++, selfCompany.getId());
            query.setParameter(index++, incomingCompanyId);
        }
        if (StringUtils.isNotEmpty(dictValue)) {
            query.setParameter(index++, dictValue);
        }
        List list = query.getResultList();
        return list;
    }

    /**
     * 検査管理項目に対する検査区分及び、検査区分毎のエビデンスファイル登録設定删除
     *
     * @param componentInspectionItemsTableId
     * @return
     */
    @Transactional
    public BasicResponse deleteMstComponentInspectionItemsTableClass(String componentInspectionItemsTableId) {
        BasicResponse basicResponse = new BasicResponse();
        Query query = entityManager.createNamedQuery("MstComponentInspectionItemsTableClass.deleteByComponentInspectionItemsTableId");
        query.setParameter("componentInspectionItemsTableId", componentInspectionItemsTableId);
        query.executeUpdate();
        return basicResponse;
    }

    /**
     * Get list inspection file name
     * @return
     */
    public List<MstComponentInspectFileName> getMstInspectFileName() {
        return entityManager.createNamedQuery("MstComponentInspectFileName.findByOwnerCompanyId", MstComponentInspectFileName.class)
            .setParameter("ownerCompanyId", "SELF")
            .getResultList();
    }

    @Transactional
    public void updateInspectFileName(MstComponentInspectFileName inspectFileName, LoginUser loginUser) {
        Query query = entityManager.createNamedQuery("MstComponentInspectFileName.updateNameById");
        query.setParameter("id", inspectFileName.getPk().getId());
        query.setParameter("fileName", inspectFileName.getFileName());
        query.setParameter("updateDate", new java.util.Date());
        query.setParameter("updateUserUuid", loginUser.getUserUuid());
        query.setParameter("ownerCompanyId", "SELF");
        query.executeUpdate();
        updBatUpdStatus(inspectFileName.getPk().getId(), BatUpdStatus.NotPushed);
    }

    public Map<String, String> convertInsFileName() {
        Map<String, String> result = new HashMap<>();
        List<MstComponentInspectFileName> inspecFileName = this.getMstInspectFileName();
        if (inspecFileName != null) {
            for (MstComponentInspectFileName mstComponentInspectFileName : inspecFileName) {
                result.put(mstComponentInspectFileName.getPk().getId(), mstComponentInspectFileName.getFileName());
            }
        }

        return result;
    }
    
    /**
     * PKでMstComponentInspectFileを取得する。
     * @param inspectTypeId
     * @param inspectClassId
     * @param ownerCompanyId
     * @return 
     */
    public Optional<MstComponentInspectFile> findFileByPK(String inspectTypeId, String inspectClassId, String ownerCompanyId) {
        return entityManager.createNamedQuery("MstComponentInspectFile.findByPK", MstComponentInspectFile.class)
            .setParameter("inspectTypeId", inspectTypeId)
            .setParameter("inspectClassId", inspectClassId)
            .setParameter("ownerCompanyId", ownerCompanyId)
            .getResultList().stream().findFirst();
    }
    
    public Optional<MstComponentInspectClass> findClassByPK(String inspectClassId, String ownerCompanyId) {
        return entityManager.createNamedQuery("MstComponentInspectClass.findById", MstComponentInspectClass.class)
            .setParameter("id", inspectClassId)
            .setParameter("ownerCompanyId", ownerCompanyId)
            .getResultList().stream().findFirst();
    }
    
    public List<MstComponentInspectFileName> findFileNameByOwnerCompanyId(String ownerCompanyId) {
        return entityManager.createNamedQuery("MstComponentInspectFileName.findByOwnerCompanyId", MstComponentInspectFileName.class)
            .setParameter("ownerCompanyId", ownerCompanyId).getResultList();
    }
    
    public List<MstComponentInspectClass> findNotPushedClass(String companyId) {
        return entityManager.createNamedQuery("MstComponentInspectClass.findNotPushed", MstComponentInspectClass.class)
            .setParameter("companyId", companyId)
            .setParameter("batUpdStatus", BatUpdStatus.Pushed).getResultList();
    }
    
    public List<MstComponentInspectType> findNotPushedType(String companyId) {
        return entityManager.createNamedQuery("MstComponentInspectType.findNotPushed", MstComponentInspectType.class)
            .setParameter("companyId", companyId)
            .setParameter("batUpdStatus", BatUpdStatus.Pushed).getResultList();
    }
    
    public List<MstComponentInspectFile> findByClassOrType(List<String> classids, List<String> typeids, String ownerCompanyId) {
        return entityManager.createNamedQuery("MstComponentInspectFile.findByClassOrType", MstComponentInspectFile.class)
            .setParameter("inspectTypeIds", typeids)
            .setParameter("inspectClassIds", classids)
            .setParameter("ownerCompanyId", ownerCompanyId)
            .getResultList();
    }
    
    public List<MstComponentInspectFileName> findNotPushedFileName(String companyId) {
        return entityManager.createNamedQuery("MstComponentInspectFileName.findNotPushed", MstComponentInspectFileName.class)
            .setParameter("companyId", companyId)
            .setParameter("batUpdStatus", BatUpdStatus.Pushed).getResultList();
    }
    
    public List<MstComponentInspectLang> findLangsByDictKeys(List<String> dictKeys) {
        if(dictKeys.isEmpty()) {
            return new ArrayList<>();
        }
        return entityManager.createNamedQuery("MstComponentInspectLang.findByDictKeys", MstComponentInspectLang.class)
            .setParameter("dictKeys", dictKeys).getResultList();
    }
    
    @Transactional
    public void extPushFileSettings(MstFileSettingsForBat fileSettings, String ownerComanyId, String userid) {
        fileSettings.getInspLangs().forEach(lang -> {
            entityManager.merge(lang);
        });
        
        fileSettings.getInspClasses().forEach(cls -> {
            cls.getPk().setOwnerCompanyId(ownerComanyId);
            entityManager.merge(cls);
        });
        
        fileSettings.getInspTypes().forEach(type -> {
            type.setOwnerCompanyId(ownerComanyId);
            entityManager.merge(type);
        });
        
        fileSettings.getFileNames().forEach(name -> {
            MstComponentInspectFileName iName = new MstComponentInspectFileName();
            iName.getPk().setId(name.getId());
            iName.getPk().setOwnerCompanyId(ownerComanyId);
            iName.setFileName(name.getFileName());
            iName.setCreateDate(new Date());
            iName.setUpdateDate(new Date());
            iName.setCreateUserUuid(userid);
            iName.setUpdateUserUuid(userid);
            entityManager.merge(iName);
        });
        
        fileSettings.getInspFiles().forEach(file -> {
            file.getMstComponentInspectFilePK().setOwnerCompanyId(ownerComanyId);
            entityManager.merge(file);
        });
    }
    
    /**
     * fileSettingsのClasses、Types、FileNamesのbatUpdStatusをPushedにしてpersistします。
     * @param fileSettings
     * @param companyId
     */
    @Transactional
    public void updateBatUpdStatusPushed(MstFileSettingsForBat fileSettings, String companyId) {
        fileSettings.getInspClasses().forEach(cls -> {
            MstInspectExtBatchStatus batState = new MstInspectExtBatchStatus(cls.getPk().getId(), companyId);
            batState.setBatUpdStatus(BatUpdStatus.Pushed);
            entityManager.merge(batState);
        });
        
        fileSettings.getInspTypes().forEach(type -> {
            MstInspectExtBatchStatus batState = new MstInspectExtBatchStatus(type.getId(), companyId);
            batState.setBatUpdStatus(BatUpdStatus.Pushed);
            entityManager.merge(batState);
        });
        
        fileSettings.getFileNames().forEach(name -> {
            MstInspectExtBatchStatus batState = new MstInspectExtBatchStatus(name.getId(), companyId);
            batState.setBatUpdStatus(BatUpdStatus.Pushed);
            entityManager.merge(batState);
        });
    }
    
    /**
     * InspectClassあるいはInspectTypeが削除可能かどうかを取得する。
     * @param id
     * @return 
     */
    private boolean isInspectClassTypeDeletable(String id) {
        return entityManager.createNamedQuery("MstInspectExtBatchStatus.findById", MstInspectExtBatchStatus.class)
            .setParameter("id", id).getResultList().isEmpty();
    }
    
    public String getInspDictVal(String langId, String dictKey) {
        return entityManager.createNamedQuery("MstComponentInspectLang.findByPk", MstComponentInspectLang.class)
            .setParameter("langId", langId)
            .setParameter("dictKey", dictKey).getResultList().stream()
            .map(lang -> lang.getDictValue()).findFirst().orElse("");
    }
    
    private List<MstComponentInspectClass> findByOwner(String ownerCompanyId, String langId) {
        List<MstComponentInspectClass> result = entityManager.createNamedQuery("MstComponentInspectClass.findByOwner", MstComponentInspectClass.class)
            .setParameter("ownerCompanyId", ownerCompanyId).setParameter("langId", langId).getResultList();
        if(result.size() > 0) {
            return result;
        }
        return entityManager.createNamedQuery("MstComponentInspectClass.findByOwner", MstComponentInspectClass.class)
            .setParameter("ownerCompanyId", "SELF").setParameter("langId", langId).getResultList();
    }
}
