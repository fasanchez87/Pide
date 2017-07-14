package com.ingeniapps.pide.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.ingeniapps.pide.activity.Detalle;
import com.ingeniapps.pide.beans.Compra;
import com.ingeniapps.pide.beans.Pedido;
import com.ingeniapps.pide.beans.Producto;
import com.ingeniapps.pide.R;
import com.ingeniapps.pide.beans.Producto;
import com.ingeniapps.pide.fragment.Historial;
import com.ingeniapps.pide.sharedPreferences.gestionSharedPreferences;
import com.ingeniapps.pide.vars.vars;
import com.ingeniapps.pide.volley.ControllerSingleton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class HistorialAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>
{
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<Pedido> listadoPedidos;
    private ArrayList<Producto> listaProductos;

    public final int TYPE_NOTICIA=0;
    public final int TYPE_LOAD=1;
    private gestionSharedPreferences sharedPreferences;
    private Context context;
    OnLoadMoreListener loadMoreListener;
    boolean isLoading=false, isMoreDataAvailable=true;
    vars vars;


    public HistorialAdapter(Activity activity, ArrayList<Pedido> listadoPedidos)
    {
        this.activity=activity;
        this.listadoPedidos=listadoPedidos;
        vars=new vars();
        sharedPreferences=new gestionSharedPreferences(this.activity);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType==TYPE_NOTICIA)
        {
            return new PedidosHolder(inflater.inflate(R.layout.historial_pedido_row,parent,false));
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
            ((PedidosHolder)holder).bindData(listadoPedidos.get(position));
        }

        final Pedido pedido = listadoPedidos.get(position);

        //EVENTO CLICK EN CADA ITEM DE PEDIDO
        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                Log.i("evento",""+pedido.getNomEmpresa());

                listaProductos=pedido.getListaProductosPedido();
                sharedPreferences.putListProductos("productosComprados",listaProductos);
                Intent intent=new Intent(v.getContext(), Detalle.class);
                v.getContext().startActivity(intent);

            }
        });
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
        return listadoPedidos.size();
    }

    private String fechaPedido;

    public class PedidosHolder extends RecyclerView.ViewHolder
    {
        public ImageView imagenEmpresaHistorialCompra;
        public TextView nombreEmpresaHistorialCompra, fechaSolicitudProductoHistorial, fechaAceptadoProductoHistorial, fechaDespachoProductoHistorial,
                fechaEntregadoProductoHistorial,fechaCanceladoProductoHistorial,labelPedidoAceptadoHistorial,
                labelPedidoDespachadoHistorial, labelPedidoEntregadoHistorial, labelPedidoCanceladoHistorial;
        public LinearLayout linearCancelacionPedidoCliente;

        private NumberFormat numberFormat=NumberFormat.getNumberInstance(Locale.GERMAN);


        public PedidosHolder(View view)
        {
            super(view);
            imagenEmpresaHistorialCompra=(ImageView) view.findViewById(R.id.imagenEmpresaHistorialCompra);

            nombreEmpresaHistorialCompra=(TextView) view.findViewById(R.id.nombreEmpresaHistorialCompra);
            //FECHAS ESTADOS PEDIDO
            fechaSolicitudProductoHistorial=(TextView) view.findViewById(R.id.fechaSolicitudProductoHistorial);
            fechaAceptadoProductoHistorial=(TextView) view.findViewById(R.id.fechaAceptadoProductoHistorial);
            fechaDespachoProductoHistorial=(TextView) view.findViewById(R.id.fechaDespachoProductoHistorial);
            fechaEntregadoProductoHistorial=(TextView) view.findViewById(R.id.fechaEntregadoProductoHistorial);
            fechaCanceladoProductoHistorial=(TextView) view.findViewById(R.id.fechaCanceladoProductoHistorial);
            linearCancelacionPedidoCliente=(LinearLayout) view.findViewById(R.id.linearCancelacionPedidoCliente);
            //LABEL DE COLOR ESTADO PEDIDO CON NOMBRE DEL ESTADO
            labelPedidoAceptadoHistorial= (TextView) view.findViewById(R.id.labelPedidoAceptadoHistorial);
            labelPedidoDespachadoHistorial=(TextView) view.findViewById(R.id.labelPedidoDespachadoHistorial);
            labelPedidoEntregadoHistorial=(TextView) view.findViewById(R.id.labelPedidoEntregadoHistorial);
            labelPedidoCanceladoHistorial=(TextView) view.findViewById(R.id.labelPedidoCanceladoClienteHistorial);
        }

        void bindData(final Pedido pedido)
        {

            if (pedido.getImgEmpresa().toString().equals("http://fasttrackcenter.com/pide/ws/images/"))
            {
                imagenEmpresaHistorialCompra.setImageResource(R.drawable.ic_not_image_found);
            }

            else
            {
                Glide.with(activity).
                        load(pedido.getImgEmpresa().toString()).
                        thumbnail(0.5f).into(imagenEmpresaHistorialCompra);
            }

            nombreEmpresaHistorialCompra.setText(pedido.getNomEmpresa());


            // Converting timestamp into x ago format
            //convert unix epoch timestamp (seconds) to milliseconds
            long timestamp = Long.parseLong(pedido.getFechaAceptado()) * 1000L;
            CharSequence fechaSolicitud = DateUtils.getRelativeTimeSpanString(timestamp,
                    System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
            fechaSolicitudProductoHistorial.setText(fechaSolicitud);

            //fechaAceptadoProductoHistorial.setText(pedido.getFechaAceptado());

            if(pedido.getFechaDespacho()!="false")
            {
                long timestampDespacho = Long.parseLong(pedido.getFechaDespacho()) * 1000L;
                CharSequence fechaDespacho = DateUtils.getRelativeTimeSpanString(timestampDespacho,
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                fechaDespachoProductoHistorial.setText(fechaDespacho);
            }

            else
            {
                fechaDespachoProductoHistorial.setText("Pendiente");
            }

            if(pedido.getFechaEntregado()!="false")
            {
                long timestampEntrega = Long.parseLong(pedido.getFechaEntregado()) * 1000L;
                CharSequence fechaEntrega = DateUtils.getRelativeTimeSpanString(timestampEntrega,
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                fechaEntregadoProductoHistorial.setText(fechaEntrega);
            }

            else
            {
                fechaEntregadoProductoHistorial.setText("Pendiente");
            }


            if(pedido.getFechaCancelado()!="false")
            {
                linearCancelacionPedidoCliente.setVisibility(View.VISIBLE);

                long timestampCancelado = Long.parseLong(pedido.getFechaCancelado()) * 1000L;
                CharSequence fechaCancela = DateUtils.getRelativeTimeSpanString(timestampCancelado,
                        System.currentTimeMillis(), DateUtils.SECOND_IN_MILLIS);
                fechaCanceladoProductoHistorial.setText(fechaCancela);
                fechaEntregadoProductoHistorial.setText("Cancelado");
                fechaDespachoProductoHistorial.setText("Cancelado");

            }



            if(pedido.getNomEstado().equals("Despachado"))
            {
                labelPedidoAceptadoHistorial.setVisibility(View.GONE);
                labelPedidoDespachadoHistorial.setVisibility(View.VISIBLE);
            }

            if(TextUtils.equals(pedido.getNomEstado(),"Entregado"))
            {
                labelPedidoDespachadoHistorial.setVisibility(View.GONE);
                labelPedidoEntregadoHistorial.setVisibility(View.VISIBLE);
                labelPedidoAceptadoHistorial.setVisibility(View.GONE);

            }

            if(TextUtils.equals(pedido.getNomEstado(),"Cancelado"))
            {
                labelPedidoAceptadoHistorial.setVisibility(View.GONE);
                labelPedidoDespachadoHistorial.setVisibility(View.GONE);
                labelPedidoEntregadoHistorial.setVisibility(View.GONE);
                labelPedidoCanceladoHistorial.setVisibility(View.VISIBLE);
            }


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

    public ArrayList<Pedido> getPedidoList()
    {
        return listadoPedidos;
    }

    public interface OnItemClickListener
    {
        void onItemClick(Pedido item);
    }

}
