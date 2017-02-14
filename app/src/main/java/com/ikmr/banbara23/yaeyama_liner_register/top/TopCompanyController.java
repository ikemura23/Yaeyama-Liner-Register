package com.ikmr.banbara23.yaeyama_liner_register.top;

import com.ikmr.banbara23.yaeyama_liner_register.entity.Liner;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Result;
import com.ikmr.banbara23.yaeyama_liner_register.entity.Status;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.company.CompanyStatus;
import com.ikmr.banbara23.yaeyama_liner_register.entity.top.company.CompanyStatusInfo;

import java.util.List;

/**
 * トップの会社別運航情報を作成
 */
public class TopCompanyController {
    private Result aneiResult;
    private Result ykfResult;
    private Result dreamResult;

    /**
     * コンストラクタ
     *
     * @param aneiResult
     * @param ykfResult
     * @param dreamResult
     */
    public TopCompanyController(Result aneiResult, Result ykfResult, Result dreamResult) {
        this.aneiResult = aneiResult;
        this.ykfResult = ykfResult;
        this.dreamResult = dreamResult;
    }

    /**
     * 全会社の運航状態の作成
     *
     * @return 全会社の運航状態クラス
     */
    public CompanyStatusInfo createCompanyStatuses() {
        CompanyStatusInfo companyStatusInfo = new CompanyStatusInfo();

        // 安栄
        companyStatusInfo.setAneiStatus(createCompanyStatus(aneiResult));
        // Ykf
        companyStatusInfo.setYkfStatus(createCompanyStatus(ykfResult));
        // ドリーム
        companyStatusInfo.setDreamStatus(createCompanyStatus(dreamResult));

        return companyStatusInfo;
    }

    /**
     * 各会社別の運航情報を作成
     *
     * @param result
     * @return
     */
    private CompanyStatus createCompanyStatus(Result result) {
        if (result == null) return null;
        CompanyStatus companyStatus = new CompanyStatus();
        companyStatus.setCompany(result.getCompany());
        companyStatus.setStatus(getStatus(result.getLiners()));
        return companyStatus;
    }

    /**
     * ステータス判定
     *
     * @param liners
     * @return
     */
    private Status getStatus(List<Liner> liners) {
        for (Liner liner : liners) {
            if (liner.getStatus() == Status.CANCEL) {
                return Status.CANCEL;    // １件でも欠航があればその会社は「欠航あり」
            } else if (liner.getStatus() == Status.SUSPEND) {
                return Status.SUSPEND;   // １件でも運休があればその会社は「未定あり」
            } else if (liner.getStatus() == Status.CAUTION) {
                return Status.CAUTION;   // １件でも未定があればその会社は「未定あり」
            }
        }
        return Status.NORMAL;   // 通常運航
    }
}
