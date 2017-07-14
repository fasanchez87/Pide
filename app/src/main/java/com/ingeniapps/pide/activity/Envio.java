package com.ingeniapps.pide.activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Paint;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
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
import com.ingeniapps.pide.adapter.CompraAdapter;
import com.ingeniapps.pide.adapter.ProductoAdapter;
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

public class Envio extends AppCompatActivity
{

    private gestionSharedPreferences sharedPreferences;
    public com.ingeniapps.pide.vars.vars vars;
    private String versionActualApp;
    private NumberFormat numberFormat;

    private EditText edit_text_direccion_envio;
    private EditText edit_text_alguna_novedad;
    private CheckBox check_aceptar_terminos;
    private TextView valorAhorroEnvio;
    private TextView valorTotalCompraEnvio;
    Button botonAceptarEnvio;
    private ArrayList<Producto> listadoCodigosProductosSeleccionados;
    private String codigosProductos;
    private String productoSeleccionados;

    private ProgressDialog progressDialog;



    private String valorAhorroTotalEnvio,valorTotalEnvio;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_envio);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        listadoCodigosProductosSeleccionados=new ArrayList<Producto>();

        codigosProductos="";
        productoSeleccionados="";

        edit_text_direccion_envio=(EditText) findViewById(R.id.edit_text_direccion_envio);
        edit_text_alguna_novedad=(EditText)findViewById(R.id.edit_text_alguna_novedad);
        check_aceptar_terminos=(CheckBox) findViewById(R.id.check_aceptar_terminos);
        valorAhorroEnvio=(TextView) findViewById(R.id.valorAhorroEnvio);
        valorTotalCompraEnvio=(TextView)findViewById(R.id.valorTotalCompraEnvio);
        botonAceptarEnvio=(Button)findViewById(R.id.botonAceptarEnvio);


        numberFormat = NumberFormat.getNumberInstance(Locale.GERMAN);

        sharedPreferences=new gestionSharedPreferences(this);

        vars=new vars();

        if (savedInstanceState==null)
        {
            Bundle extras = getIntent().getExtras();
            if (extras==null)
            {
                valorAhorroTotalEnvio = null;
                valorTotalEnvio = null;
            }
            else
            {
                valorAhorroTotalEnvio = extras.getString("valorAhorro");
                valorTotalEnvio = extras.getString("valorTotal");
            }
        }

        valorAhorroEnvio.setText("$"+numberFormat.format(Double.parseDouble(valorAhorroTotalEnvio)));
        valorTotalCompraEnvio.setText("$"+numberFormat.format(Double.parseDouble(valorTotalEnvio)));

        sharedPreferences.getListProductos("productosPedidos",Producto.class);
        JSONObject producto;
        JSONArray productos = new JSONArray();
        JSONObject pedido = new JSONObject();
        JSONObject productosSeleccionados = new JSONObject();
        JSONObject nomProducto = new JSONObject();

        for (int i = 0; i < sharedPreferences.getListProductos("productosPedidos", Producto.class).size(); i++)
        {
            if(sharedPreferences.getListProductos("productosPedidos",Producto.class).get(i) != null )
            {
                producto = new JSONObject();
                nomProducto = new JSONObject();

                try
                {
                    producto.put("id",sharedPreferences.getListProductos("productosPedidos", Producto.class).get(i).getIdProducto());
                    producto.put("nombre",sharedPreferences.getListProductos("productosPedidos", Producto.class).get(i).getNombreProducto());
                    producto.put("cantidadsolicitada",sharedPreferences.getListProductos("productosPedidos", Producto.class).get(i).getNumeroDeProducto());
                    producto.put("valorunitario",sharedPreferences.getListProductos("productosPedidos", Producto.class).get(i).getPrecioPideProducto());
                    producto.put("ahorrounitario",sharedPreferences.getListProductos("productosPedidos", Producto.class).get(i).getAhorroProducto());
                    productos.put(producto);

                }
                catch (JSONException e)
                {
                    e.printStackTrace();
                }

                Log.i("Productos:", "Imagen: " + sharedPreferences.getListProductos("productosPedidos", Producto.class).get(i).getImagenProducto());
                Log.i("Productos:", "Producto: " + sharedPreferences.getListProductos("productosPedidos", Producto.class).get(i).getNombreProducto());
                Log.i("Productos:", "Cantidad: " + sharedPreferences.getListProductos("productosPedidos", Producto.class).get(i).getNumeroDeProducto());
                Log.i("Productos:", "Valor Unitario: " + sharedPreferences.getListProductos("productosPedidos", Producto.class).get(i).getPrecioPideProducto());
            }
        }

        try
        {
            productosSeleccionados.put("productos", productos);
            pedido.put("pedido",productosSeleccionados);
        }
        catch (JSONException e)
        {
            e.printStackTrace();
        }


        productoSeleccionados=pedido.toString();
        Log.i("JSON:",""+productoSeleccionados);


        //codigosProductos=codigosProductos.substring(0, codigosProductos.lastIndexOf(","));
        //Log.i("Codigos:", "Codigo: " + codigosProductos);

        botonAceptarEnvio.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Envio.this, R.style.AlertDialogTheme));
                builder
                        .setTitle("Pide")
                        .setMessage("¿Estás seguro de realizar el pedido, no se te queda algo?")
                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                                WebServiceEnviarPedido();
                            }
                        }).setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {

                    }
                }).show();


            }

    });

    }

    private void WebServiceEnviarPedido()
    {

        progressDialog = new ProgressDialog(new android.view.ContextThemeWrapper(Envio.this,R.style.AppCompatAlertDialogStyle));
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Registrando tu pedido, espera un momento");
        progressDialog.show();
        progressDialog.setCancelable(false);

        String _urlWebService = vars.ipServer.concat("/ws/Pedido");

        JsonObjectRequest jsonObjReq = new JsonObjectRequest(Request.Method.POST, _urlWebService, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            if(response.getBoolean("status"))
                            {
                                progressDialog.dismiss();

                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Envio.this,R.style.AlertDialogTheme));
                                builder
                                        .setTitle("Pide")
                                        .setMessage("Su pedido ha sido registrado exitosamente. ¡Te notificaremos cuando se proceda al despacho!")
                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id)
                                            {
                                                sharedPreferences.remove("productosPedidos");
                                                Intent intent = new Intent(getApplicationContext(), Principal.class);
                                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                                startActivity(intent);
                                            }
                                        }).setCancelable(false).show();
                            }

                            else
                            {

                                progressDialog.dismiss();



                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Envio.this,R.style.AlertDialogTheme));
                                builder
                                        .setTitle("Pide")
                                        .setMessage("Error al realizar el pedido, intenta de nuevo o dirigete a la opcion soporte y en minutos te contactaremos.")
                                        .setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id)
                                            {

                                            }
                                        }).setCancelable(false).show();
                            }
                        }
                        catch (JSONException e)
                        {

                            progressDialog.dismiss();


                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Envio.this,R.style.AlertDialogTheme));
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

                            progressDialog.dismiss();


                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Envio.this,R.style.AlertDialogTheme));
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
                            progressDialog.dismiss();


                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Envio.this,R.style.AlertDialogTheme));
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

                            progressDialog.dismiss();


                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Envio.this,R.style.AlertDialogTheme));
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

                            progressDialog.dismiss();


                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Envio.this,R.style.AlertDialogTheme));
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
                            progressDialog.dismiss();


                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Envio.this,R.style.AlertDialogTheme));
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
                            progressDialog.dismiss();


                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Envio.this,R.style.AlertDialogTheme));
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
                headers.put("codCliente", "1");
                headers.put("direccionEnvio", "PRUEBA APP");
                headers.put("novedadEnvio", "PRUEBA APP");
                headers.put("productos", productoSeleccionados);
                headers.put("valorAhorroTotalEnvio", valorAhorroTotalEnvio);
                headers.put("valorTotalEnvio", valorTotalEnvio);
              /*  headers.put("versionApp",versionActualApp);
                headers.put("MyToken", sharedPreferences.getString("MyToken"));*/
                return headers;
            }
        };

        ControllerSingleton.getInstance().addToReqQueue(jsonObjReq, "");
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }

}
