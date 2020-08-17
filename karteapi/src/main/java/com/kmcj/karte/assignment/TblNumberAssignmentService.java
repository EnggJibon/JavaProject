/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.kmcj.karte.assignment;

import com.kmcj.karte.constants.CommonConstants;
import javax.enterprise.context.Dependent;
import javax.persistence.EntityManager;
import javax.persistence.LockModeType;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

/**
 * 申請番号は申請先の会社で採番されたものが返却される。カルテ間データ連携で送信された申請の申請番号はAD2+9ケタ連番(※)とする。
 * <P>
 * ※ADはAsset Disposition(資産廃棄) ※サプライヤーが自社の所有資産の申請管理も行いたい場合に、
 * <p>
 * 他社に申請して採番された申請番号と重複しないようにするためADの後の数字を分ける。
 * <P>
 * 資産廃棄申請受付登録(後述)で直接自社のデータベースに登録した申請の申請番号はAD1+9ケタ連番とする。
 *
 */
/**
 *
 * @author admin
 */
@Dependent
public class TblNumberAssignmentService {

    @PersistenceContext(unitName = CommonConstants.PERSISTENCE_UNIT_NAME)
    private EntityManager entityManager;

    public TblNumberAssignmentService() {
    }

    /**
     *
     * @param itemName
     * 
     * @return
     */
    public String findByKey(String itemName) {

        Query query = entityManager.createNamedQuery("TblNumberAssignment.findByItemName");
        query.setParameter("itemName", itemName);

        // 排他処理のため、採番テーブルをロックする
        query.setLockMode(LockModeType.PESSIMISTIC_WRITE);

        TblNumberAssignment tblNumberAssignment = (TblNumberAssignment) query.getSingleResult();

        // プレフィックスを取得
        String startStr = tblNumberAssignment.getItemNamePrefix();

        // SEQのサイズを取得
        int size = tblNumberAssignment.getDigits();

        StringBuilder seqKey = new StringBuilder();

        if (size > startStr.length()) {

            int numberSize = size - startStr.length();

            int seqNum = tblNumberAssignment.getCurrentNumber() + 1;

            int seqNumSize = String.valueOf(tblNumberAssignment.getCurrentNumber() + 1).length();

            // SEQのサイズが超える場合
            if (seqNumSize > numberSize) {

                return "";
            }

            seqKey.append(startStr);

            // SEQの桁数を補足
            if (seqNumSize < numberSize) {

                seqKey.append(String.format("%0" + numberSize + "d", seqNum));

            } else {

                seqKey.append(String.valueOf(seqNum));
            }

        }

        return seqKey.toString();

    }

    /**
     *
     * @param itemName
     * @param userUuid
     */
    public void updateTblNumberAssignment(String itemName, String userUuid) {

        Query query = entityManager.createNamedQuery("TblNumberAssignment.findByItemName");
        query.setParameter("itemName", itemName);

        TblNumberAssignment tblNumberAssignment = (TblNumberAssignment) query.getSingleResult();

        // 更新用の項目を設定する
        tblNumberAssignment.setCurrentNumber(tblNumberAssignment.getCurrentNumber() + 1);

        java.util.Date updateDate = new java.util.Date();
        tblNumberAssignment.setUpdateDate(updateDate);
        tblNumberAssignment.setUpdateUserUuid(userUuid);

        entityManager.merge(tblNumberAssignment);

    }

}
