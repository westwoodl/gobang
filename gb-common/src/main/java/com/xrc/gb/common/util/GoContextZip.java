package com.xrc.gb.common.util;

import com.xrc.gb.common.dto.go.GoContext;
import com.xrc.gb.common.dto.go.GoPieces;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

/**
 * @author xu rongchao
 * @date 2020/4/16 10:04
 */
public class GoContextZip {
    private GoContextZip(){}

    private final static String GO_CONTEXT = "&";
    private final static String LIST_STR = ";";
    /**
     * 19&dj0;ej0;di0;ei0;eh0;fh0;fg0;gg0;gf0;he0;cj0;
     */
    public static String zip(GoContext goContext) {
        StringBuilder zip = new StringBuilder();
        Integer size = goContext.getCheckerBoardSize();
        zip.append(size).append(GO_CONTEXT);
        for (GoPieces goPieces : goContext.getPlaceArrays()) {
            zip.append(goPieces.getPieceType());
            zip.append((char)(goPieces.getX() + 'a'));
            zip.append((char)(goPieces.getY() + 'a'));
            zip.append(goPieces.isDead() ? 1 : 0);
            zip.append(";");
        }
        return zip.toString();
    }

    public static GoContext unzip(String str) {
        GoContext goContext = new GoContext();
        goContext.setPlaceArrays(new ArrayList<>());
        String[] strarr1 = str.split(GO_CONTEXT);
        goContext.setCheckerBoardSize(Integer.valueOf(strarr1[0]));
        String[] strarr2 = strarr1[1].split(LIST_STR);
        int hand = 1;
        for (String s : strarr2) {
            if (StringUtils.isNotBlank(s)) {
                GoPieces goPieces = new GoPieces();
                goPieces.setPieceType(Integer.parseInt(String.valueOf(s.charAt(0))));
                goPieces.setX(s.charAt(1) - 'a');
                goPieces.setY(s.charAt(2) - 'a');
                goPieces.setDead(s.charAt(3) == 1);
                // todo type
                goPieces.setHandNum(hand++);
                goContext.getPlaceArrays().add(goPieces);
            }
        }
        return goContext;
    }

    public static void main(String[] args) {
        String s = "B[qd];W[pp];B[cc];W[cp];B[nc];W[fp];B[qq];W[pq];B[qp];W[qn];B[qo];W[po];B[rn];W[qr];B[rr];W[rm];B[pr];W[or];B[pn];W[qm];B[qs];W[on];B[dj];W[nk];B[ph];W[ch];B[cf];W[eh];B[ci];W[de];B[df];W[dc];B[cd];W[dd];B[ef];W[di];B[ei];W[dh];B[cj];W[ce];B[be];W[bf];B[bg];W[bd];B[af];W[bc];B[fi];W[cm];B[hq];W[ek];B[fh];W[gq];B[hp];W[ej];B[eq];W[gr];B[cq];W[dp];B[dq];W[ep];B[bp];W[bh];B[ah];W[bo];B[bq];W[fg];B[gg];W[kp];B[ko];W[jo];B[jn];W[in];B[jp];W[io];B[lp];W[kq];B[lq];W[kr];B[lr];W[ir];B[kn];W[il];B[oq];W[pf];B[nh];W[rf];B[od];W[qi];B[qg];W[rd];B[qf];W[qe];B[pe];W[re];B[qc];W[rg];B[kh];W[ic];B[gc];W[kc];B[jd];W[id];B[ge];W[hb];B[gb];W[jf];B[je];W[ie];B[ld];W[hg];B[eg];W[lc];B[le];W[hf];B[qh];W[rh];B[pi];W[qj];B[gk];W[fd];B[gd];W[lf];B[mf];W[lg];B[gm];W[gn];B[fn];W[go];B[dl];W[mo];B[oo];W[pm];B[op];W[mg];B[nf];W[lo];B[nn];W[lm];B[pn];W[dk];B[ck];W[cl];B[el];W[bk];B[bi];W[li];B[ii];W[ds];B[dr];W[hi];B[ik];W[jk];B[ij];W[md];B[mc];W[ke];B[me];W[kd];B[om];W[ls];B[ms];W[ks];B[nr];W[ng];B[og];W[es];B[cs];W[fr];B[er];W[fs];B[bs];W[hl];B[pl];W[ql];B[rc];W[ro];B[rp];W[sn];B[hm];W[im];B[kk];W[kj];B[lk];W[jl];B[mj];W[mi];B[nj];W[pk];B[fm];W[cn];B[ol];W[ok];B[ni];W[ih];B[ji];W[mb];B[nb];W[lb];B[fe];W[cb];B[mp];W[mm];B[eb];W[na];B[oa];W[ma];B[qb];W[bj];B[ai];W[aj];B[ag];W[gl];B[fk];W[bl];B[kg];W[kf];B[ib];W[jb];B[ga];W[ha];B[ed];W[ec];B[fc];W[gf];B[ff];W[gj];B[hk];W[hh];B[fj];W[no];B[fq];W[hr];B[kl];W[km];B[mn];W[ln];B[nl];W[db];B[da];W[ca];B[ea];W[np];B[nq];W[oj];B[oi];W[en];B[em];W[eo];B[dm];W[dn];B[sp];W[so];B[hn];W[ho];B[hc];W[ia];B[ao];W[an];B[ap];W[sc];B[sb];W[sd];B[jg];W[ad];B[gh];W[ae];B[ee];W[ml];B[mk];W[pj];B[bf];W[nm];B[on];W[he];B[ig];W[ki];B[jh];W[fl];B[jj];W[fo];B[hj];W[gi];B[ll];W[jm];B[lh];W[mh];B[lj];W[if];B[hd])";
        System.out.println();
    }
}
