package com.ingeniapps.pide.activity;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ingeniapps.pide.R;
import com.ingeniapps.pide.adapter.CompraAdapter;
import com.ingeniapps.pide.adapter.DetalleAdapter;
import com.ingeniapps.pide.adapter.HistorialAdapter;
import com.ingeniapps.pide.adapter.ProductoAdapter;
import com.ingeniapps.pide.beans.Producto;
import com.ingeniapps.pide.sharedPreferences.gestionSharedPreferences;
import com.ingeniapps.pide.vars.vars;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class Detalle extends AppCompatActivity
{

    private gestionSharedPreferences sharedPreferences;
    private ArrayList<Producto> listaPedidosCargada;
    private ArrayList<Producto> listaPedidosInicial;
    public com.ingeniapps.pide.vars.vars vars;
    private RecyclerView recycler_view_detalle_productos;
    private DetalleAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    RelativeLayout layoutEspera;
    RelativeLayout layoutMacroEsperaPedidos;
    ImageView not_found_productos;
    private int pagina;
    Context context;
    private boolean solicitando=false;
    //VERSION DEL APP INSTALADA
    private String versionActualApp;
    private static TextView ui_hot = null;
    private NumberFormat numberFormat;


    public ImageView imagenDescripcionNoticia,botonIncrementarProducto,botonDecrementarProducto;
    Button botonHacerPedidoCompra;
    private TextView valorSubtotalCompra;
    private TextView valorDescuentoCompra;
    private TextView valorDomicilioCompra;
    private TextView valorTotalCompra;

    private double precioPideTotales;
    private double precioPideTotalesAhorro;
    private int cantidadProductos;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        numberFormat = NumberFormat.getNumberInstance(Locale.GERMAN);
        valorDescuentoCompra=(TextView)findViewById(R.id.valorDescuentoDetalle);
        valorTotalCompra=(TextView)findViewById(R.id.valorTotalDetalle);

        precioPideTotales=0;
        precioPideTotalesAhorro=0;
        cantidadProductos=0;

        sharedPreferences=new gestionSharedPreferences(this);
        listaPedidosInicial=new ArrayList<Producto>();
        listaPedidosCargada=new ArrayList<Producto>();
        vars=new vars();
        context = this;
        pagina=0;

        not_found_productos=(ImageView)findViewById(R.id.not_found_noticias);
        //not_found_noticias.setVisibility(View.VISIBLE);

        recycler_view_detalle_productos=(RecyclerView) findViewById(R.id.recycler_view_detalle_productos);
        mLayoutManager = new LinearLayoutManager(this);

        listaPedidosInicial=sharedPreferences.getListProductos("productosComprados",Producto.class);

        for (int i = 0; i < listaPedidosInicial.size(); i++)
        {
            if(listaPedidosInicial.get(i) != null )
            {
                Log.i("Detalle:", "Imagen: " + listaPedidosInicial.get(i).getImagenProducto());
                Log.i("Detalle:", "Producto: " + listaPedidosInicial.get(i).getNombreProducto());
                Log.i("Detalle:", "Cantidad: " + listaPedidosInicial.get(i).getNumeroDeProducto());
                Log.i("Detalle:", "Cantidad: " + listaPedidosInicial.get(i).getPrecioPideProducto());
                Log.i("Detalle:", "Ahorro: " + listaPedidosInicial.get(i).getAhorroProducto());
                Log.i("Detalle:", "Codigo: " + listaPedidosInicial.get(i).getIdProducto());

                Producto producto = new Producto();
                //AGREGAMOS PARA PONER EL PROGRESS AL FINAL DE PANTALLA
                //noticia.setType(listaPedidosInicial.getString("type"));//type==evento
                //DATOS EVENTO
                producto.setImagenProducto(listaPedidosInicial.get(i).getImagenProducto());
                producto.setNombreProducto(listaPedidosInicial.get(i).getNombreProducto());
                producto.setNumeroDeProducto(listaPedidosInicial.get(i).getNumeroDeProducto());
                producto.setPrecioPideProducto(listaPedidosInicial.get(i).getPrecioPideProducto());

                precioPideTotalesAhorro+=(Double.parseDouble(listaPedidosInicial.get(i).getAhorroProducto())*Double.parseDouble(listaPedidosInicial.get(i).getNumeroDeProducto()));

                precioPideTotales+=Double.parseDouble(listaPedidosInicial.get(i).getNumeroDeProducto())*Double.parseDouble(listaPedidosInicial.get(i).getPrecioPideProducto());

                listaPedidosCargada.add(producto);

            }
        }

        mAdapter = new DetalleAdapter(this,listaPedidosCargada);

        recycler_view_detalle_productos.setHasFixedSize(true);
        recycler_view_detalle_productos.setLayoutManager(mLayoutManager);
        recycler_view_detalle_productos.setItemAnimator(new DefaultItemAnimator());
        recycler_view_detalle_productos.setAdapter(mAdapter);

        valorDescuentoCompra.setText("$"+numberFormat.format(precioPideTotalesAhorro));
        valorTotalCompra.setText("$"+numberFormat.format(precioPideTotales));



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_compra, menu);
        return true;
    }






}
