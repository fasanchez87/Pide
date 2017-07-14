package com.ingeniapps.pide.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Paint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.bumptech.glide.Glide;
import com.ingeniapps.pide.R;
import com.ingeniapps.pide.beans.Producto;
import com.ingeniapps.pide.fragment.Empresas;
import com.ingeniapps.pide.sharedPreferences.gestionSharedPreferences;
import com.ingeniapps.pide.vars.vars;
import com.ingeniapps.pide.volley.ControllerSingleton;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

public class ProductoAdapter extends BaseAdapter
{
    private Activity activity;
    private LayoutInflater inflater;
    private ArrayList<Producto> listadoProductos;
    private ArrayList<Producto> productosPedidos;

    Empresas pedido;

    public final int TYPE_NOTICIA=0;
    public final int TYPE_LOAD=1;
    private gestionSharedPreferences sharedPreferences;
    private Context context;
/*
    OnLoadMoreListener loadMoreListener;
*/
    boolean isLoading=false, isMoreDataAvailable=true;
    vars vars;
    public static int contador;

    public static int i;
    public static int contadorGeneral;
    private NumberFormat numberFormat;
    ImageLoader imageLoader = ControllerSingleton.getInstance().getImageLoader();

    public ProductoAdapter(Activity activity, ArrayList<Producto> listadoProductos)
    {
        this.activity=activity;
        this.listadoProductos=listadoProductos;
        productosPedidos=new ArrayList<Producto>();
        vars=new vars();
        sharedPreferences=new gestionSharedPreferences(this.activity);
        contador=0;
        contadorGeneral=0;
    }

   /* @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if(viewType==TYPE_NOTICIA)
        {
            return new NoticiaHolder(inflater.inflate(R.layout.producto_row_layout,parent,false));
        }
        else
        {
            return new LoadHolder(inflater.inflate(R.layout.row_load,parent,false));
        }
    }*/

    /*@Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position)
    {
        if(position >= getItemCount()-1 && isMoreDataAvailable && !isLoading && loadMoreListener!=null)
        {
            isLoading = true;
            loadMoreListener.onLoadMore();
        }

        if(getItemViewType(position)==TYPE_NOTICIA)
        {
            ((NoticiaHolder)holder).bindData(listadoProductos.get(position));
        }
    }*/

    @Override
    public int getCount()
    {
        return listadoProductos.size();
    }

    @Override
    public Producto getItem(int position)
    {
        return listadoProductos.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup)
    {
        numberFormat = NumberFormat.getNumberInstance(Locale.GERMAN);

        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.producto_row_layout, viewGroup, false);
        }

        final Producto producto = getItem(position);

        ImageView imagenProducto = (ImageView) view.findViewById(R.id.imagenProducto);

        TextView nombreProducto = (TextView) view.findViewById(R.id.nombreProducto);
        TextView cantidadProducto = (TextView) view.findViewById(R.id.cantidadProducto);
        TextView precioGeneralProducto = (TextView) view.findViewById(R.id.precioGeneralProducto);
        TextView precioPideProducto = (TextView) view.findViewById(R.id.precioPideProducto);
        TextView ahorroProducto = (TextView) view.findViewById(R.id.ahorroProducto);

        if (producto.getImagenProducto().toString().equals("http://fasttrackcenter.com/pide/ws/images/"))
        {
            imagenProducto.setImageResource(R.drawable.ic_not_image_found);
        }

        else
        {
            Glide.with(activity).
                    load(producto.getImagenProducto().toString()).
                    thumbnail(0.5f).into(imagenProducto);
        }

        nombreProducto.setText(producto.getNombreProducto()+" "+producto.getCantidadProducto());

        //cantidadProducto.setText(producto.getCantidadProducto());
        precioGeneralProducto.setText("$"+numberFormat.format(Double.parseDouble(producto.getPrecioGeneralProducto())));
        precioGeneralProducto.setPaintFlags(precioGeneralProducto.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
        precioPideProducto.setText("$"+numberFormat.format(Double.parseDouble(producto.getPrecioPideProducto())));
        int valorahorro=(Integer.parseInt(producto.getPrecioGeneralProducto()))-(Integer.parseInt(producto.getPrecioPideProducto()));
        ahorroProducto.setText(""+"$"+numberFormat.format(Double.parseDouble(""+valorahorro)));


        return view;
    }

    /*@Override
    public int getItemViewType(int position)
    {
        if(listadoProductos.get(position).getType().equals("producto"))
        {
            Log.i("TYPE","NOTICIA");
            return TYPE_NOTICIA;
        }
        else
        {
            Log.i("TYPE","LOAD");
            return TYPE_LOAD;
        }
    }*/

   /* @Override
    public int getItemCount()
    {
        return listadoProductos.size();
    }*/

    private int counter=0;

  /*  public class NoticiaHolder extends RecyclerView.ViewHolder
    {
        //public NetworkImageView imagenPerfil;
        public ImageView imagenProducto;
        public TextView EmpresaProducto, nombreProducto, cantidadProducto, precioGeneralProducto,contadorProducto,ahorroPorcentaje, precioPideProducto,ahorroProducto,precioGxaxseneralProducto;
        public ImageView imagenDescripcionNoticia,botonIncrementarProducto,botonDecrementarProducto;

        public NoticiaHolder(View view)
        {
            super(view);
            imagenProducto=(ImageView) view.findViewById(R.id.imagenProducto);
            EmpresaProducto=(TextView) view.findViewById(R.id.EmpresaProducto);
            nombreProducto=(TextView) view.findViewById(R.id.nombreProducto);
            cantidadProducto = (TextView) view.findViewById(R.id.cantidadProducto);
            precioGeneralProducto = (TextView) view.findViewById(R.id.precioGeneralProducto);
            precioPideProducto = (TextView) view.findViewById(R.id.precioPideProducto);
            ahorroProducto = (TextView) view.findViewById(R.id.ahorroProducto);
            contadorProducto = (TextView) view.findViewById(R.id.contadorProducto);
            botonIncrementarProducto = (ImageView) view.findViewById(R.id.botonIncrementarProducto);
            botonDecrementarProducto = (ImageView) view.findViewById(R.id.botonDecrementarProducto);
            precioGxaxseneralProducto = (TextView) view.findViewById(R.id.precioGxaxseneralProducto);
            ahorroPorcentaje = (TextView) view.findViewById(R.id.ahorroPorcentaje);

        }

        public void bindData(final Producto producto)
        {


            numberFormat = NumberFormat.getNumberInstance(Locale.GERMAN);

            if (producto.getImagenProducto().toString().equals("http://fasttrackcenter.com/pide/ws/images/"))
            {
                *//*Glide.with(activity).
                        load("http://fasttrackcenter.com/pide/ws/images/not_found_image.png").
                        thumbnail(0.5f).into(imagenProducto);*//*

                imagenProducto.setImageResource(R.drawable.ic_not_image_found);
            }

            else
            {
                Glide.with(activity).
                        load(producto.getImagenProducto().toString()).
                        thumbnail(0.5f).into(imagenProducto);
            }

            //BOTON QUITAR PRODUCTO
            botonDecrementarProducto.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //CONTADOR CARRITO
                    contadorGeneral--;

                    if(i==0 || contadorProducto.getText().equals("0"))
                    {
                        contadorProducto.setText("0");
                        contadorGeneral=0;
                        //pedido.updateHotCount(0);
                        //botonDecrementarProducto.setEnabled(false);
                        return;
                    }

                    botonDecrementarProducto.setEnabled(true);

                    i=Integer.parseInt(contadorProducto.getText().toString());
                    i--;
                    contadorProducto.setText(String.valueOf(i));
                    producto.setNumeroDeProducto(String.valueOf(i));
                    //pedido.updateHotCount(contadorGeneral);

                    //productosPedidos.remove((Integer.parseInt(producto.getIdProducto())-1));
                    sharedPreferences.putListProductos("productos",productosPedidos);

                }
            });

            imagenProducto.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {

                    Toast.makeText(ProductoAdapter.this.activity,"Id item: "+(Integer.parseInt(producto.getIdProducto())),Toast.LENGTH_LONG).show();
                    Toast.makeText(ProductoAdapter.this.activity,"Count: "+productosPedidos.size(),Toast.LENGTH_LONG).show();

                    *//* Intent intent = new Intent(v.getContext(), DetalleNoticia.class);
                    intent.putExtra("nomEmpresa",noticia.getNomEmpresa());
                    intent.putExtra("nomCiudad",noticia.getNomCiudad());
                    intent.putExtra("fecNoticia",noticia.getTimeStampItemNoticia());
                    intent.putExtra("desLargaEvento",noticia.getDesLargaNoticia());
                    intent.putExtra("codVideo",noticia.getVidNoticia());
                    intent.putExtra("imgEvento",noticia.getImgNoticia());
                    intent.putExtra("codEvento",noticia.getCodNoticia());
                    intent.putExtra("imgEmpresa",noticia.getImgEmpresa());
                    v.getContext().startActivity(intent);*//*
                }
            });

            EmpresaProducto.setText(producto.getEmpresaProducto());
            nombreProducto.setText(producto.getNombreProducto());
            cantidadProducto.setText(producto.getCantidadProducto());

            precioGeneralProducto.setText("$"+numberFormat.format(Double.parseDouble(producto.getPrecioGeneralProducto())));
            precioGeneralProducto.setPaintFlags(precioGeneralProducto.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);
            precioGxaxseneralProducto.setPaintFlags(precioGxaxseneralProducto.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

            precioPideProducto.setText("$"+numberFormat.format(Double.parseDouble(producto.getPrecioPideProducto())));
            int valorahorro=(Integer.parseInt(producto.getPrecioGeneralProducto()))-(Integer.parseInt(producto.getPrecioPideProducto()));
            ahorroProducto.setText(""+"$"+numberFormat.format(Double.parseDouble(""+valorahorro)));

            //CALCULAMOS % DE AHORRO
            double diferencia=(Double.parseDouble(producto.getPrecioGeneralProducto()))-(Double.parseDouble(producto.getPrecioPideProducto()));
            double porcentaje=(diferencia)/(Double.parseDouble(producto.getPrecioGeneralProducto()));
            double porcen=((porcentaje)*100);
            String ahorro=""+porcen;
            ahorro=""+ahorro.substring(0,3);
            ahorroPorcentaje.setText(""+ahorro+" %");

            contadorProducto.setText(producto.getNumeroDeProducto());
            // Converting timestamp into x ago format
            //convert unix epoch timestamp (seconds) to milliseconds

            //BOTON AGREGAR PRODUCTO
            botonIncrementarProducto.setOnClickListener(new View.OnClickListener()
            {
                @Override
                public void onClick(View v)
                {
                    //CONTADOR CARRITO
                    contadorGeneral++;

                    Producto p=new Producto();

                    p.setIdProducto(producto.getIdProducto());
                    p.setImagenProducto(producto.getImagenProducto());
                    p.setNombreProducto(producto.getNombreProducto());
                    p.setCantidadProducto(producto.getCantidadProducto());
                    p.setPrecioGeneralProducto(producto.getPrecioGeneralProducto());
                    p.setPrecioPideProducto(producto.getPrecioPideProducto());
                    p.setAhorroProducto(producto.getAhorroProducto());
                    //CONTADOR DE PRODUCTO
                    i=Integer.parseInt(contadorProducto.getText().toString());
                    i++;
                    sharedPreferences.putInt("contadorProducto",i);
                    contadorProducto.setText(String.valueOf(i));
                    producto.setNumeroDeProducto(String.valueOf(i));
//                    pedido.updateHotCount(contadorGeneral);
                    //CANTIDAD DE PRODUCTOS
                    producto.setNumeroDeProducto(contadorProducto.getText().toString());
                    //AGREGAMOS EL PRODUCTO A LISTADO
                    productosPedidos.add(producto);
                    sharedPreferences.putListProductos("productos",productosPedidos);
                }
            });


        }
    }*/

    /*static class LoadHolder extends RecyclerView.ViewHolder
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
    *//* notifyDataSetChanged is final method so we can't override it
        call adapter.notifyDataChanged(); after update the list
        *//*
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

    public ArrayList<Producto> getProductosList()
    {
        return listadoProductos;
    }*/

}
