package com.ingeniapps.pide.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.ingeniapps.pide.R;
import com.ingeniapps.pide.adapter.EndlessRecyclerViewScrollListener;
import com.ingeniapps.pide.adapter.HistorialAdapter;
import com.ingeniapps.pide.beans.Pedido;
import com.ingeniapps.pide.beans.Producto;
import com.ingeniapps.pide.sharedPreferences.gestionSharedPreferences;
import com.ingeniapps.pide.vars.vars;
import com.ingeniapps.pide.volley.ControllerSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Historial extends Fragment
{
    private gestionSharedPreferences sharedPreferences;
    private ArrayList<Pedido> listadoPedidos;
    private ArrayList<Producto> listadoProductos;
    public vars vars;
    private RecyclerView recycler_view_noticias;
    private HistorialAdapter mAdapter;
    LinearLayoutManager mLayoutManager;
    RelativeLayout layoutEspera;
    RelativeLayout layoutMacroEsperaNoticias;
    ImageView not_found_noticias;
    private int pagina;
    Context context;
    private boolean solicitando=false;
    //VERSION DEL APP INSTALADA
    private String versionActualApp;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        sharedPreferences=new gestionSharedPreferences(getActivity().getApplicationContext());
        listadoPedidos=new ArrayList<Pedido>();
        listadoProductos=new ArrayList<Producto>();
        vars=new vars();
        context = getActivity();
        pagina=0;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        not_found_noticias=(ImageView)getActivity().findViewById(R.id.not_found_noticias);
        //not_found_noticias.setVisibility(View.VISIBLE);
        layoutEspera=(RelativeLayout)getActivity().findViewById(R.id.layoutEsperaHistorialPedidos);
        layoutMacroEsperaNoticias=(RelativeLayout)getActivity().findViewById(R.id.layoutMacroEsperaHistorialPedidos);
        recycler_view_noticias=(RecyclerView) getActivity().findViewById(R.id.recycler_view_historial_pedidos);
        mLayoutManager = new LinearLayoutManager(getActivity());

        mAdapter = new HistorialAdapter(getActivity(),listadoPedidos);

        recycler_view_noticias.setHasFixedSize(true);
        recycler_view_noticias.setLayoutManager(mLayoutManager);
        recycler_view_noticias.setItemAnimator(new DefaultItemAnimator());
        recycler_view_noticias.setAdapter(mAdapter);

/*
        EndlessRecyclerViewScrollListener scrollListener = new EndlessRecyclerViewScrollListener(mLayoutManager)
        {
            @Override
            public void onLoadMore(final int page, final int totalItemsCount, final RecyclerView view)
            {
                final int curSize = mAdapter.getItemCount();

                view.post(new Runnable()
                {
                    @Override
                    public void run()
                    {
                        if(!solicitando)
                        {
                            WebServiceGetNoticiasMore();
                        }
                    }
                });
            }
        };*/

        //VERSION APP
        try
        {
            versionActualApp=getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        //recycler_view_noticias.addOnScrollListener(scrollListener);
        WebServiceGetHistorial();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_historial, container, false);
    }

    private void WebServiceGetHistorial()
    {
        listadoPedidos.clear();
        String _urlWebService = vars.ipServer.concat("/ws/GetPedidoCliente");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.GET, _urlWebService, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            if(response.getBoolean("status"))
                            {
                                layoutMacroEsperaNoticias.setVisibility(View.GONE);
                                recycler_view_noticias.setVisibility(View.VISIBLE);

                                JSONArray listaPedidos = response.getJSONArray("pedidos");

                                for (int i = 0; i < listaPedidos.length(); i++)
                                {
                                    JSONObject jsonObject = (JSONObject) listaPedidos.get(i);

                                    Pedido pedido = new Pedido();
                                    //AGREGAMOS PARA PONER EL PROGRESS AL FINAL DE PANTALLA
                                    //noticia.setType(jsonObject.getString("type"));//type==evento
                                    //DATOS EVENTO
                                    pedido.setImgEmpresa(jsonObject.getString("imgEmpresa"));
                                    pedido.setNomEmpresa(jsonObject.getString("nomEmpresa"));
                                    pedido.setFechaAceptado(jsonObject.getString("fecPedido"));
                                    pedido.setFechaDespacho(jsonObject.getString("fecDespachado"));
                                    pedido.setFecEntregado(jsonObject.getString("fecEntregaPedido"));
                                    pedido.setFechaCancelado(jsonObject.getString("fecCancelaPedido"));
                                    pedido.setNomEstado(jsonObject.getString("nomEstadoPed"));

                                    Log.i("nomEmpresa",jsonObject.getString("fecCancelaPedido"));

                                    listadoProductos=new ArrayList<Producto>();

                                    for (int p=0; p<jsonObject.getJSONArray("productos").length(); p++)
                                    {
                                        JSONObject producto = (JSONObject) jsonObject.getJSONArray("productos").get(p);

                                        Producto product=new Producto();
                                        product.setIdProducto(producto.getString("codProducto"));
                                        product.setCodEmpresa(producto.getString("codEmpresa"));
                                        product.setImagenProducto(producto.getString("imgProducto"));
                                        product.setNombreProducto(producto.getString("nomProducto"));
                                        product.setCantidadProducto(producto.getString("cantProducto"));
                                        product.setPrecioGeneralProducto(producto.getString("preGeneralProducto"));
                                        product.setPrecioPideProducto(producto.getString("prePideProducto"));
                                        product.setAhorroProducto(producto.getString("preAhorroProducto"));
                                        product.setNumeroDeProducto(producto.getString("numProducto"));
                                        Log.i("nomEmpresa",producto.getString("nomProducto"));

                                        listadoProductos.add(product);
                                    }

                                    pedido.setListaProductosPedido(listadoProductos);
                                    listadoPedidos.add(pedido);
                                }

                               //pagina+=1;

                            }

                            else
                            {
                                layoutMacroEsperaNoticias.setVisibility(View.VISIBLE);
                                layoutEspera.setVisibility(View.GONE);
                                not_found_noticias.setVisibility(View.VISIBLE);
                            }
                        }
                        catch (JSONException e)
                        {
                            layoutMacroEsperaNoticias.setVisibility(View.VISIBLE);
                            layoutEspera.setVisibility(View.GONE);
                            not_found_noticias.setVisibility(View.VISIBLE);

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Historial.this.getActivity(),R.style.AlertDialogTheme));
                            builder
                                    .setMessage(e.getMessage().toString())
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                        }
                                    }).show();

                            e.printStackTrace();
                        }
                        mAdapter.notifyDataSetChanged();
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        if (error instanceof TimeoutError)
                        {
                            layoutMacroEsperaNoticias.setVisibility(View.VISIBLE);
                            layoutEspera.setVisibility(View.GONE);
                            not_found_noticias.setVisibility(View.VISIBLE);

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Historial.this.getActivity(),R.style.AlertDialogTheme));
                            builder
                                    .setMessage("Error de conexión, sin respuesta del servidor.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                        }
                                    }).show();
                        }

                        else

                        if (error instanceof NoConnectionError)
                        {
                            layoutMacroEsperaNoticias.setVisibility(View.VISIBLE);
                            layoutEspera.setVisibility(View.GONE);
                            not_found_noticias.setVisibility(View.VISIBLE);

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Historial.this.getActivity(),R.style.AlertDialogTheme));
                            builder
                                    .setMessage("Por favor, conectese a la red.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                        }
                                    }).show();
                        }

                        else

                        if (error instanceof AuthFailureError)
                        {
                            layoutMacroEsperaNoticias.setVisibility(View.VISIBLE);
                            layoutEspera.setVisibility(View.GONE);
                            not_found_noticias.setVisibility(View.VISIBLE);

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Historial.this.getActivity(),R.style.AlertDialogTheme));
                            builder
                                    .setMessage("Error de autentificación en la red, favor contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                        }
                                    }).show();
                        }

                        else

                        if (error instanceof ServerError)
                        {
                            layoutMacroEsperaNoticias.setVisibility(View.VISIBLE);
                            layoutEspera.setVisibility(View.GONE);
                            not_found_noticias.setVisibility(View.VISIBLE);

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Historial.this.getActivity(),R.style.AlertDialogTheme));
                            builder
                                    .setMessage("Error server, sin respuesta del servidor.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                        }
                                    }).show();
                        }

                        else

                        if (error instanceof NetworkError)
                        {
                            layoutMacroEsperaNoticias.setVisibility(View.VISIBLE);
                            layoutEspera.setVisibility(View.GONE);
                            not_found_noticias.setVisibility(View.VISIBLE);

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Historial.this.getActivity(),R.style.AlertDialogTheme));
                            builder
                                    .setMessage("Error de red, contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                        }
                                    }).show();
                        }

                        else

                        if (error instanceof ParseError)
                        {
                            layoutMacroEsperaNoticias.setVisibility(View.VISIBLE);
                            layoutEspera.setVisibility(View.GONE);
                            not_found_noticias.setVisibility(View.VISIBLE);

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Historial.this.getActivity(),R.style.AlertDialogTheme));
                            builder
                                    .setMessage("Error de conversión Parser, contacte a su proveedor de servicios.")
                                    .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                        }
                                    }).show();
                        }
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap <String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("WWW-Authenticate", "xBasic realm=".concat(""));
                headers.put("codCliente", "1");
               /* headers.put("codUsuario", sharedPreferences.getString("codUsuario"));
                headers.put("tokenFCM", sharedPreferences.getString("tokenFCM"));
                headers.put("versionApp",versionActualApp);*/
                headers.put("MyToken", sharedPreferences.getString("MyToken"));
                return headers;
            }
        };

        ControllerSingleton.getInstance().addToReqQueue(jsonObjReq, "");
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }



}
