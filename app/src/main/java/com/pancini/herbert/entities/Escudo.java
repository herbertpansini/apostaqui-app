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
            case "BGT" :
                this.icon = R.drawable.bgt;
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
            case "SPT" :
                this.icon = R.drawable.spt;
                break;
            default:
                break;
        }
        return this.icon;
    }
}
