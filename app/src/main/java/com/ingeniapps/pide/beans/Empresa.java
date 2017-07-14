package com.ingeniapps.pide.beans;

/**
 * Created by FABiO on 28/06/2017.
 */

public class Empresa
{

    private String nomEmpresa;

    public String getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(String codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    private String codEmpresa;

    public String getNomEmpresa() {
        return nomEmpresa;
    }

    public void setNomEmpresa(String nomEmpresa) {
        this.nomEmpresa = nomEmpresa;
    }

    public String getImaEmpresa() {
        return imaEmpresa;
    }

    public void setImaEmpresa(String imaEmpresa) {
        this.imaEmpresa = imaEmpresa;
    }

    private String imaEmpresa;

    public Empresa()
    {

    }

    public Empresa(String nomEmpresa, String imaEmpresa)
    {
        this.nomEmpresa=nomEmpresa;
        this.imaEmpresa=imaEmpresa;
    }
}
