package com.ingeniapps.pide.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.ingeniapps.pide.beans.Compra;
import com.ingeniapps.pide.beans.Producto;
import com.ingeniapps.pide.R;
import com.ingeniapps.pide.beans.Producto;
import com.ingeniapps.pide.sharedPreferences.gestionSharedPreferences;
import com.ingeniapps.pide.vars.vars;
import com.ingeniapps.pide.volley.ControllerSingleton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class DetalleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<Producto> listadoProductos;

    public final int TYPE_NOTICIA=0;
    public final int TYPE_LOAD=1;
    private gestionSharedPreferences sharedPreferences;
    private Context context;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading=false, isMoreDataAvailable=true;
    vars vars;

    ImageLoader imageLoader = ControllerSingleton.getInstance().getImageLoader();

    public DetalleAdapter(Activity activity, ArrayList<Producto> listadoProductos)
    {
        this.activity=activity;
        this.listadoProductos=listadoProductos;
        vars=new vars();
        sharedPreferences=new gestionSharedPreferences(this.activity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType==TYPE_NOTICIA)
        {
            return new CompraHolder(inflater.inflate(R.layout.detalle_row_layout,parent,false));
        }
        else
        {
            return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if(position >= getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null)
        {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if(getItemViewType(position)==TYPE_NOTICIA)
        {
            ((CompraHolder)holder).bindData(listadoProductos.get(position));
        }
    }

    @Override
    public int getItemViewType(int position)
    {
        /*if(listadoProductos.get(position).getType().equals("noticia"))
        {
            Log.i("TYPE","NOTICIA");
            return TYPE_NOTICIA;
        }
        else
        {
            Log.i("TYPE","LOAD");
            return TYPE_LOAD;
        }*/
        return 0;
    }

    @Override
    public int getItemCount()
    {
        return listadoProductos.size();
    }

    public class CompraHolder extends RecyclerView.ViewHolder
    {
        public ImageView imagenProductoCompra;
        public TextView nombreProductoCompra, cantidadProductoCompra, valorUnitarioProductoCompra, valorTotalCompraUnitaria;

        private NumberFormat numberFormat=NumberFormat.getNumberInstance(Locale.GERMAN);


        public CompraHolder(View view)
        {
            super(view);
            imagenProductoCompra=(ImageView) view.findViewById(R.id.imagenProductoDetalle);
            nombreProductoCompra=(TextView) view.findViewById(R.id.nombreProductoDetalle);
            cantidadProductoCompra=(TextView) view.findViewById(R.id.cantidadProductoDetalle);
            valorUnitarioProductoCompra = (TextView) view.findViewById(R.id.valorUnitarioProductoDetalle);
            valorTotalCompraUnitaria = (TextView) view.findViewById(R.id.valorTotalCompraUnitariaDetalle);
        }

        void bindData(final Producto compra)
        {

            Log.i("imagen",""+compra.getImagenProducto().toString());

            if (compra.getImagenProducto().toString().equals("http://fasttrackcenter.com/pide/ws/images/"))
            {
                imagenProductoCompra.setImageResource(R.drawable.ic_not_image_found);
            }

            else
            {
                Glide.with(activity).
                        load(compra.getImagenProducto().toString()).
                        thumbnail(0.5f).into(imagenProductoCompra);
            }

            nombreProductoCompra.setText(compra.getNombreProducto());
            cantidadProductoCompra.setText(compra.getNumeroDeProducto());//UNIDADES SELECCIONADAS
            valorUnitarioProductoCompra.setText("$"+numberFormat.format(Double.parseDouble(compra.getPrecioPideProducto())));
            double valorTotal=Double.parseDouble(compra.getNumeroDeProducto())*Double.parseDouble(compra.getPrecioPideProducto());
            valorTotalCompraUnitaria.setText("$"+numberFormat.format(valorTotal));
        }
    }

    static class LoadHolder extends RecyclerView.ViewHolder
    {
        public LoadHolder(View itemView)
        {
            super(itemView);
        }
    }

    public void setMoreDataAvailable(boolean moreDataAvailable)
    {
        isMoreDataAvailable = moreDataAvailable;
    }
    /* notifyDataSetChanged is final method so we can't override it
        call adapter.notifyDataChanged(); after update the list
        */
    public void notifyDataChanged()
    {
        notifyDataSetChanged();
        isLoading = false;
    }

    public interface OnLoadMoreListener
    {
        void onLoadMore();
    }

    public void setLoadMoreListener(OnLoadMoreListener loadMoreListener)
    {
        this.loadMoreListener = loadMoreListener;
    }

    public ArrayList<Producto> getCompraList()
    {
        return listadoProductos;
    }

}
