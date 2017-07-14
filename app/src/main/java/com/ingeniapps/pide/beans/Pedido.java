package com.ingeniapps.pide.beans;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by FABiO on 12/06/2017.
 */

public class Pedido
{

    String idProducto;
    String imagenProducto;
    String nombreProducto;
    String cantidadProducto;//LIBRAS/ML/UNIDADES
    String precioGeneralProducto;
    String precioPideProducto;
    String ahorroProducto;
    String numeroDeProducto;//CONTADOR DE PRODUCTO




    public String getNomEstado() {
        return nomEstado;
    }

    public void setNomEstado(String nomEstado) {
        this.nomEstado = nomEstado;
    }


    //DATOS PEDIDO SOLICITADO
    String codPedido;
    String codEstado;
    String nomEstado;
    String codUsuario;
    String codCliente;
    String valorPedido;
    String valorAhorro;
    String direccionPedido;
    String novedadPedido;

    String fechaSolicitudPedido;
    String fechaAceptado;
    String fechaDespacho;
    String fechaEntregado;
    String fechaCancelado;

    //DATOS EMPRESA GESTION PEDIDO
    String codEmpresa;
    String nomEmpresa;

    //ESTADOS PEDIDO
    String nomEstadoPed;

    //PRECIOS Y VALORES PEDIDO
    String valorAhorroPedido;
    String valorTotalPedido;


    public String getNomEmpresa() {
        return nomEmpresa;
    }

    public void setNomEmpresa(String nomEmpresa) {
        this.nomEmpresa = nomEmpresa;
    }

    public String getImgEmpresa() {
        return imgEmpresa;
    }

    public void setImgEmpresa(String imgEmpresa) {
        this.imgEmpresa = imgEmpresa;
    }

    public String getFechaEntregado() {
        return fechaEntregado;
    }

    public void setFechaEntregado(String fechaEntregado) {
        this.fechaEntregado = fechaEntregado;
    }

    public String getNomEstadoPed() {
        return nomEstadoPed;
    }

    public void setNomEstadoPed(String nomEstadoPed) {
        this.nomEstadoPed = nomEstadoPed;
    }

    String imgEmpresa;
    //PRODUCTOS DEL PEDIDO
    ArrayList<Producto> listaProductosPedido;

    public String getFechaAceptado() {
        return fechaAceptado;
    }

    public void setFechaAceptado(String fechaAceptado) {
        this.fechaAceptado = fechaAceptado;
    }

    public String getCodPedido() {
        return codPedido;
    }

    public void setCodPedido(String codPedido) {
        this.codPedido = codPedido;
    }

    public String getCodEstado() {
        return codEstado;
    }

    public void setCodEstado(String codEstado) {
        this.codEstado = codEstado;
    }

    public String getCodUsuario() {
        return codUsuario;
    }

    public void setCodUsuario(String codUsuario) {
        this.codUsuario = codUsuario;
    }

    public String getCodCliente() {
        return codCliente;
    }

    public void setCodCliente(String codCliente) {
        this.codCliente = codCliente;
    }

    public String getValorPedido() {
        return valorPedido;
    }

    public void setValorPedido(String valorPedido) {
        this.valorPedido = valorPedido;
    }

    public String getValorAhorro() {
        return valorAhorro;
    }

    public void setValorAhorro(String valorAhorro) {
        this.valorAhorro = valorAhorro;
    }

    public String getDireccionPedido() {
        return direccionPedido;
    }

    public void setDireccionPedido(String direccionPedido) {
        this.direccionPedido = direccionPedido;
    }

    public String getValorAhorroPedido() {
        return valorAhorroPedido;
    }

    public void setValorAhorroPedido(String valorAhorroPedido) {
        this.valorAhorroPedido = valorAhorroPedido;
    }

    public String getValorTotalPedido() {
        return valorTotalPedido;
    }

    public void setValorTotalPedido(String valorTotalPedido) {
        this.valorTotalPedido = valorTotalPedido;
    }

    public String getNovedadPedido() {
        return novedadPedido;
    }

    public void setNovedadPedido(String novedadPedido) {
        this.novedadPedido = novedadPedido;
    }

    public String getFechaSolicitudPedido() {
        return fechaSolicitudPedido;
    }

    public void setFechaSolicitudPedido(String fechaSolicitudPedido) {
        this.fechaSolicitudPedido = fechaSolicitudPedido;
    }

    public String getFechaDespacho() {
        return fechaDespacho;
    }

    public void setFechaDespacho(String fechaDespacho) {
        this.fechaDespacho = fechaDespacho;
    }

    public String getFecEntregado() {
        return fechaEntregado;
    }

    public void setFecEntregado(String fechaEntregado) {
        this.fechaEntregado = fechaEntregado;
    }

    public String getFechaCancelado() {
        return fechaCancelado;
    }

    public void setFechaCancelado(String fechaCancelado) {
        this.fechaCancelado = fechaCancelado;
    }

    public ArrayList<Producto> getListaProductosPedido() {
        return listaProductosPedido;
    }

    public void setListaProductosPedido(ArrayList<Producto> listaProductosPedido) {
        this.listaProductosPedido = listaProductosPedido;
    }




    public String getCodEmpresa() {
        return codEmpresa;
    }

    public void setCodEmpresa(String codEmpresa) {
        this.codEmpresa = codEmpresa;
    }

    //PARA MOSTRAR LOADING EN EL ONLOADMORE DEL RECICLER VIEW

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }


    public Pedido()
    {

    }

    public Pedido(String type)
    {
        super();
        this.type = type;
    }

    public Pedido(String idProducto, String imagenProducto, String nombreProducto, String cantidadProducto,
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

    public Pedido(String codPedido, String codEstado, String nomEstadoPed, String codUsuario, String codCliente,String valorAhorroPedido,String valorTotalPedido,
                  String valorPedido, String valorAhorro, String direccionPedido, String novedadPedido,
                  String fechaSolicitudPedido,String fechaAceptado,String fechaDespacho, String fechaEntregado, String fechaCancelado, String nomEmpresa,
                  ArrayList<Producto> listaProductosPedido)
    {
        this.codPedido=codPedido;
        this.codEstado=codEstado;
        this.codUsuario=codUsuario;//USUARIO ACEPTO SOLICITUD
        this.codCliente=codCliente;
        this.valorPedido=valorPedido;
        this.valorAhorro=valorAhorro;
        this.direccionPedido=direccionPedido;
        this.novedadPedido=novedadPedido;

        this.fechaSolicitudPedido=fechaSolicitudPedido;
        this.fechaAceptado=fechaAceptado;
        this.fechaDespacho=fechaDespacho;
        this.fechaEntregado=fechaEntregado;
        this.fechaCancelado=fechaCancelado;

        this.nomEstadoPed=nomEstadoPed;

        this.valorAhorroPedido=valorAhorroPedido;
        this.valorTotalPedido=valorTotalPedido;

        this.nomEmpresa=nomEmpresa;
        this.listaProductosPedido=listaProductosPedido;
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
