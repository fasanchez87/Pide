package com.ingeniapps.pide.fragment;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

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
import com.ingeniapps.pide.activity.Pedidos;
import com.ingeniapps.pide.adapter.EmpresaAdapter;
import com.ingeniapps.pide.adapter.ProductoAdapter;
import com.ingeniapps.pide.beans.Empresa;
import com.ingeniapps.pide.beans.Producto;
import com.ingeniapps.pide.sharedPreferences.gestionSharedPreferences;
import com.ingeniapps.pide.vars.vars;
import com.ingeniapps.pide.volley.ControllerSingleton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class Empresas extends Fragment implements AdapterView.OnItemClickListener
{
    private gestionSharedPreferences sharedPreferences;
    private ArrayList<Producto> listadoProductos;
    private ArrayList<Empresa> listadoEmpresas;
    private ArrayList<Producto> listaPedidos;
    public vars vars;
    private RecyclerView recycler_view_productos;
    private ProductoAdapter mAdapter;
    private EmpresaAdapter empresaAdapter;
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

    private GridView gridView;

    private NumberFormat numberFormat;

    public ImageView imagenDescripcionNoticia,botonIncrementarProducto,botonDecrementarProducto;

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
        sharedPreferences=new gestionSharedPreferences(getActivity().getApplicationContext());
        listadoProductos=new ArrayList<Producto>();
        listadoEmpresas=new ArrayList<Empresa>();
        listaPedidos=new ArrayList<Producto>();
        vars=new vars();
        context = getActivity();
        pagina=0;
        numberFormat = NumberFormat.getNumberInstance(Locale.GERMAN);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState)
    {
        super.onViewCreated(view, savedInstanceState);
        not_found_productos=(ImageView)getActivity().findViewById(R.id.not_found_noticias);
        //not_found_noticias.setVisibility(View.VISIBLE);
        gridView = (GridView) getActivity().findViewById(R.id.gridEmpresas);
        layoutEspera=(RelativeLayout)getActivity().findViewById(R.id.layoutEsperaProductos);
        layoutMacroEsperaPedidos=(RelativeLayout)getActivity().findViewById(R.id.layoutMacroEsperaPedidos);
        //recycler_view_productos=(RecyclerView) getActivity().findViewById(R.id.recycler_view_productos);
        mLayoutManager = new LinearLayoutManager(getActivity());
        //mAdapter = new ProductoAdapter(getActivity(),listadoProductos);
        //empresaAdapter = new EmpresaAdapter(getActivity(),listadoEmpresas);
        //gridView.setAdapter(empresaAdapter);
        gridView.setOnItemClickListener(this);

       // recycler_view_productos.setAdapter(mAdapter);



        //VERSION APP
        try
        {
            versionActualApp=getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        WebServiceGetEmpresas();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_empresas, container, false);
    }

    private void showGrid(JSONArray listaEmpresas)
    {

        for (int i=0; i<listaEmpresas.length(); i++)
        {
            JSONObject jsonObject = null;
            try
            {
                jsonObject = (JSONObject) listaEmpresas.get(i);
                Empresa empresa = new Empresa();
                empresa.setNomEmpresa(jsonObject.getString("nomEmpresa"));//type==evento
                empresa.setImaEmpresa(jsonObject.getString("imgEmpresa"));
                empresa.setCodEmpresa(jsonObject.getString("codEmpresa"));
                listadoEmpresas.add(empresa);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        //Creating GridViewAdapter Object
        EmpresaAdapter gridViewAdapter = new EmpresaAdapter(getActivity(),listadoEmpresas);
        //Adding adapter to gridview
        gridView.setAdapter(gridViewAdapter);
    }

    private void WebServiceGetEmpresas()
    {
        listadoEmpresas.clear();
        String _urlWebService = vars.ipServer.concat("/ws/getEmpresa");

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
                                layoutMacroEsperaPedidos.setVisibility(View.GONE);
                                //recycler_view_productos.setVisibility(View.VISIBLE);
                                 gridView.setVisibility(View.VISIBLE);

                                JSONArray listaEmpresas = response.getJSONArray("empresas");
                                showGrid(listaEmpresas);
                            }

                            else
                            {
                                layoutMacroEsperaPedidos.setVisibility(View.VISIBLE);
                                layoutEspera.setVisibility(View.GONE);
                                not_found_productos.setVisibility(View.VISIBLE);
                            }
                        }
                        catch (JSONException e)
                        {
                            layoutMacroEsperaPedidos.setVisibility(View.VISIBLE);
                            layoutEspera.setVisibility(View.GONE);
                            not_found_productos.setVisibility(View.VISIBLE);

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Empresas.this.getActivity(),R.style.AlertDialogTheme));
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
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        if (error instanceof TimeoutError)
                        {
                            layoutMacroEsperaPedidos.setVisibility(View.VISIBLE);
                            layoutEspera.setVisibility(View.GONE);
                            not_found_productos.setVisibility(View.VISIBLE);

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Empresas.this.getActivity(),R.style.AlertDialogTheme));
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
                            layoutMacroEsperaPedidos.setVisibility(View.VISIBLE);
                            layoutEspera.setVisibility(View.GONE);
                            not_found_productos.setVisibility(View.VISIBLE);

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Empresas.this.getActivity(),R.style.AlertDialogTheme));
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

                        if (error instanceof AuthFailureError)
                        {
                            layoutMacroEsperaPedidos.setVisibility(View.VISIBLE);
                            layoutEspera.setVisibility(View.GONE);
                            not_found_productos.setVisibility(View.VISIBLE);

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Empresas.this.getActivity(),R.style.AlertDialogTheme));
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

                        if (error instanceof ServerError)
                        {
                            layoutMacroEsperaPedidos.setVisibility(View.VISIBLE);
                            layoutEspera.setVisibility(View.GONE);
                            not_found_productos.setVisibility(View.VISIBLE);

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Empresas.this.getActivity(),R.style.AlertDialogTheme));
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

                        if (error instanceof NetworkError)
                        {
                            layoutMacroEsperaPedidos.setVisibility(View.VISIBLE);
                            layoutEspera.setVisibility(View.GONE);
                            not_found_productos.setVisibility(View.VISIBLE);

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Empresas.this.getActivity(),R.style.AlertDialogTheme));
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

                        if (error instanceof ParseError)
                        {
                            layoutMacroEsperaPedidos.setVisibility(View.VISIBLE);
                            layoutEspera.setVisibility(View.GONE);
                            not_found_productos.setVisibility(View.VISIBLE);

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Empresas.this.getActivity(),R.style.AlertDialogTheme));
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
                    }
                })
        {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError
            {
                HashMap<String, String> headers = new HashMap <String, String>();
                headers.put("Content-Type", "application/json; charset=utf-8");
                headers.put("WWW-Authenticate", "xBasic realm=".concat(""));
                headers.put("numIndex", String.valueOf(0));
                headers.put("codUsuario", sharedPreferences.getString("codUsuario"));
                headers.put("tokenFCM", sharedPreferences.getString("tokenFCM"));
                headers.put("versionApp",versionActualApp);
                headers.put("MyToken", sharedPreferences.getString("MyToken"));
                return headers;
            }
        };

        ControllerSingleton.getInstance().addToReqQueue(jsonObjReq, "");
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Empresa item = (Empresa) parent.getItemAtPosition(position);
        Intent intent = new Intent(getActivity(), Pedidos.class);
        intent.putExtra("codEmpresa",item.getCodEmpresa());
        startActivity(intent);
    }
}
