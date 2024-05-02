package com.pancini.herbert.entities;

import com.pancini.herbert.apostaqui.R;

public class Escudo {
    private int icon;

    public int getEscudo(String sigla) {
        switch (sigla) {
            case "ACG" :
                this.icon = R.drawable.acg;
                break;
            case "AME" :
                this.icon = R.drawable.ame;
                break;
            case "BAH" :
                this.icon = R.drawable.bah;
                break;
            case "BOT" :
                this.icon = R.drawable.bot;
                break;
            case "RBB" :
                this.icon = R.drawable.bgt;
                break;
            case "CRI" :
                this.icon = R.drawable.cri;
                break;
            case "CRU" :
                this.icon = R.drawable.cru;
                break;
            case "CAM" :
                this.icon = R.drawable.cam;
                break;
            case "CAP" :
                this.icon = R.drawable.cap;
                break;
            case "CEA" :
                this.icon = R.drawable.cea;
                break;
            case "CHA" :
                this.icon = R.drawable.cha;
                break;
            case "COR" :
                this.icon = R.drawable.cor;
                break;
            case "CUI" :
                this.icon = R.drawable.cui;
                break;
            case "FLA" :
                this.icon = R.drawable.fla;
                break;
            case "FLU" :
                this.icon = R.drawable.flu;
                break;
            case "FOR" :
                this.icon = R.drawable.fort;
                break;
            case "GRE" :
                this.icon = R.drawable.gre;
                break;
            case "INT" :
                this.icon = R.drawable.inter;
                break;
            case "JUV" :
                this.icon = R.drawable.juv;
                break;
            case "PAL" :
                this.icon = R.drawable.pal;
                break;
            case "SAN" :
                this.icon = R.drawable.san;
                break;
            case "SAO" :
                this.icon = R.drawable.sao;
                break;
            case "VIT" :
                this.icon = R.drawable.vit;
                break;
            case "VAS" :
                this.icon = R.drawable.vas;
                break;
            case "SPT" :
                this.icon = R.drawable.spt;
                break;
            case "ITU" :
                this.icon = R.drawable.itu;
                break;
            case "POR" :
                this.icon = R.drawable.por;
                break;
            case "STA" :
                this.icon = R.drawable.sta;
                break;
            case "GUA" :
                this.icon = R.drawable.gua;
                break;
            case "PON" :
                this.icon = R.drawable.pon;
                break;
            case "AGS" :
                this.icon = R.drawable.ags;
                break;
            case "INL" :
                this.icon = R.drawable.inl;
                break;
            case "MIR" :
                this.icon = R.drawable.mir;
                break;
            case "BSP" :
                this.icon = R.drawable.bsp;
                break;
            case "NOV" :
                this.icon = R.drawable.novo;
                break;
            case "SBD" :
                this.icon = R.drawable.sbd;
                break;
            default:
                break;
        }
        return this.icon;
    }
}
