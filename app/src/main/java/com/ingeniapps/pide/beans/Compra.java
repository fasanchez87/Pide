package com.ingeniapps.pide.beans;

/**
 * Created by FABiO on 12/06/2017.
 */

public class Compra {

    String idProducto;
    String imagenProducto;
    String nombreProducto;
    String cantidadProducto;//LIBRAS/ML/UNIDADES
    String precioGeneralProducto;
    String precioPideProducto;
    String ahorroProducto;
    String numeroDeProducto;//CONTADOR DE PRODUCTO

    public String getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(String codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    //PARA MOSTRAR LOADING EN EL ONLOADMORE DEL RECICLER VIEW
    String codEmpresa;
    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Compra()
    {

    }

    public Compra(String type)
    {
        super();
        this.type = type;
    }

    public Compra(String idProducto, String imagenProducto, String nombreProducto, String cantidadProducto,
                    String precioGeneralProducto, String precioPideProducto, String ahorroProducto, String numeroDeProducto,
                    String codEmpresa)
    {
        this.idProducto=idProducto;
        this.imagenProducto=imagenProducto;
        this.nombreProducto=nombreProducto;
        this.cantidadProducto=cantidadProducto;
        this.precioGeneralProducto=precioGeneralProducto;
        this.precioPideProducto=precioPideProducto;
        this.ahorroProducto=ahorroProducto;
        this.numeroDeProducto=numeroDeProducto;
        this.codEmpresa=codEmpresa;
    }


    public String getEmpresaProducto() {
        return empresaProducto;
    }

    public void setEmpresaProducto(String empresaProducto) {
        this.empresaProducto = empresaProducto;
    }

    String empresaProducto;

    public String getIdProducto() {
        return idProducto;
    }

    public void setIdProducto(String idProducto) {
        this.idProducto = idProducto;
    }

    public String getImagenProducto() {
        return imagenProducto;
    }

    public void setImagenProducto(String imagenProducto) {
        this.imagenProducto = imagenProducto;
    }

    public String getNombreProducto() {
        return nombreProducto;
    }

    public void setNombreProducto(String nombreProducto) {
        this.nombreProducto = nombreProducto;
    }

    public String getCantidadProducto() {
        return cantidadProducto;
    }

    public void setCantidadProducto(String cantidadProducto) {
        this.cantidadProducto = cantidadProducto;
    }

    public String getPrecioGeneralProducto() {
        return precioGeneralProducto;
    }

    public void setPrecioGeneralProducto(String precioGeneralProducto) {
        this.precioGeneralProducto = precioGeneralProducto;
    }

    public String getPrecioPideProducto() {
        return precioPideProducto;
    }

    public void setPrecioPideProducto(String precioPideProducto) {
        this.precioPideProducto = precioPideProducto;
    }

    public String getAhorroProducto() {
        return ahorroProducto;
    }

    public void setAhorroProducto(String ahorroProducto) {
        this.ahorroProducto = ahorroProducto;
    }

    public String getNumeroDeProducto() {
        return numeroDeProducto;
    }

    public void setNumeroDeProducto(String numeroDeProducto) {
        this.numeroDeProducto = numeroDeProducto;
    }




}
