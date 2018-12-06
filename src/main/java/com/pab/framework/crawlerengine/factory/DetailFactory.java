package com.pab.framework.crawlerengine.factory;

public final class DetailFactory {
    public static Detail getDetail(String domain) {
        switch (domain) {
            case "www.cbrc.gov.cn":
                return new CbrcDetail();
            case "www.nifa.org.cn":
                return new NifaDetail();
            case "www.51kaxun.com":
                return new KaxunDetail();
            case "www.iresearch.com.cn":
                return new IresearchDetail();
            case "www.analysys.cn":
                return new AnalysysDetail();
            case "www.bugutime.com":
                return new BugutimeDetail();
            case "xueqiu.com":
                return new XueQiuDetail();
            case "www.huxiu.com":
                return new HuxiuDetail();
            case "www.wdzj.com":
                return new WdzjDetail();
        }
        return null;
    }
}
