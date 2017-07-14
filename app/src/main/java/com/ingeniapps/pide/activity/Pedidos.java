package com.ingeniapps.pide.activity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

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
import com.bumptech.glide.Glide;
import com.ingeniapps.pide.R;
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
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Locale;
import java.util.Map;

public class Pedidos extends AppCompatActivity implements AdapterView.OnItemClickListener
{

    private gestionSharedPreferences sharedPreferences;
    private ArrayList<Producto> listadoProductos;
    private ArrayList<Producto> listaPedidos;
    public vars vars;
    LinearLayoutManager mLayoutManager;
    RelativeLayout layoutEspera;
    RelativeLayout layoutMacroEsperaPedidos;
    ImageView not_found_productos;
    Context context;
    //VERSION DEL APP INSTALADA
    private String versionActualApp;
    private static TextView ui_hot = null;
    private ArrayList<Producto> productosPedidos;
    private NumberFormat numberFormat;
    private GridView gridView;
    public static AlertDialog alertDialogCompra;
    //LOGICA CONTADORES
    public static int i;
    public static int contadorGeneral;
    private int sizeListaProductos;
    private String codEmpresa;
    HashMap<Integer, Producto> productosPedidos1;
    private Producto[] pruebaProductosPedidos;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pedidos);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        sharedPreferences=new gestionSharedPreferences(this);
        listadoProductos=new ArrayList<Producto>();
        listaPedidos=new ArrayList<Producto>();
        vars=new vars();
        context = this;
        numberFormat = NumberFormat.getNumberInstance(Locale.GERMAN);
        contadorGeneral=0;
        sizeListaProductos=0;

        productosPedidos=new ArrayList<Producto>();
        productosPedidos1 = new HashMap<Integer,Producto>();

        not_found_productos=(ImageView)findViewById(R.id.not_found_noticias);
        layoutEspera=(RelativeLayout)findViewById(R.id.layoutEsperaProductos);
        layoutMacroEsperaPedidos=(RelativeLayout)findViewById(R.id.layoutMacroEsperaPedidos);
        gridView = (GridView) findViewById(R.id.gridProductos);
        mLayoutManager = new LinearLayoutManager(this);

        gridView.setOnItemClickListener(this);

        if (savedInstanceState==null)
        {
            Bundle extras = getIntent().getExtras();
            if (extras == null)
            {
                codEmpresa = null;
            }
            else
            {
                codEmpresa = extras.getString("codEmpresa");
            }
        }

        //VERSION APP
        try
        {
            versionActualApp=getPackageManager().getPackageInfo(this.getPackageName(), 0).versionName;
        }
        catch (PackageManager.NameNotFoundException e)
        {
            e.printStackTrace();
        }

        WebServiceGetProductos();
    }


    private void showGrid(JSONArray listaProductos)
    {

        for (int i=0; i<listaProductos.length(); i++)
        {
            JSONObject jsonObject = null;
            try
            {
                jsonObject = (JSONObject) listaProductos.get(i);
                Producto producto = new Producto();
                producto.setIdProducto(jsonObject.getString("codProducto"));//type==evento
                producto.setImagenProducto(jsonObject.getString("imgProducto"));//type==evento
                producto.setNombreProducto(jsonObject.getString("nomProducto"));
                producto.setCantidadProducto(jsonObject.getString("cantProducto"));
                producto.setPrecioGeneralProducto(jsonObject.getString("preGeneralProducto"));
                producto.setPrecioPideProducto(jsonObject.getString("prePideProducto"));
                producto.setAhorroProducto(jsonObject.getString("preAhorroProducto"));
                listadoProductos.add(producto);
            }
            catch (JSONException e)
            {
                e.printStackTrace();
            }
        }

        ProductoAdapter gridViewAdapter = new ProductoAdapter(this,listadoProductos);
        //Adding adapter to gridview
        gridView.setAdapter(gridViewAdapter);
    }

    @Override
    public boolean onSupportNavigateUp()
    {
        sharedPreferences.remove("productosPedidos");
        finish();
        return true;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if (keyCode == KeyEvent.KEYCODE_BACK)
        {
            sharedPreferences.remove("productosPedidos");
            finish();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    public static void updateHotCount(final int new_hot_number)
    {
        if (ui_hot == null)
            return;

        if (new_hot_number == 0)
            ui_hot.setVisibility(View.INVISIBLE);
        else
        {
            ui_hot.setVisibility(View.VISIBLE);
            ui_hot.setText(Integer.toString(new_hot_number));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_carrito_compra, menu);

        final View menu_hotlist = menu.findItem(R.id.MenuActionCarritoCompras).getActionView();
        ui_hot = (TextView) menu_hotlist.findViewById(R.id.contadorCarrito);

        updateHotCount(0);

        menu_hotlist.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(sharedPreferences.getListProductos("productosPedidos",Producto.class).size()==0)
                {
                    AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Pedidos.this,R.style.AlertDialogTheme));
                    builder
                            .setTitle("Pide")
                            .setMessage("La bolsa del ahorro esta vacía, Pide!")
                            .setPositiveButton("Aceptar", new DialogInterface.OnClickListener()
                            {
                                @Override
                                public void onClick(DialogInterface dialog, int id)
                                {

                                }
                            }).show();

                    return;
                }

                else
                {
                    for (int i = 0; i < sharedPreferences.getListProductos("productosPedidos", Producto.class).size(); i++)
                    {
                        if(sharedPreferences.getListProductos("productosPedidos",Producto.class).get(i) != null )
                        {
                            Log.i("Productos:", "Imagen: " + sharedPreferences.getListProductos("productosPedidos", Producto.class).get(i).getImagenProducto());
                            Log.i("Productos:", "Producto: " + sharedPreferences.getListProductos("productosPedidos", Producto.class).get(i).getNombreProducto());
                            Log.i("Productos:", "Cantidad: " + sharedPreferences.getListProductos("productosPedidos", Producto.class).get(i).getNumeroDeProducto());
                            Log.i("Productos:", "Valor Unitario: " + sharedPreferences.getListProductos("productosPedidos", Producto.class).get(i).getPrecioPideProducto());
                        }
                    }


                    JSONObject producto;
                    JSONArray productos = new JSONArray();
                    JSONObject pedido = new JSONObject();
                    JSONObject nomProducto = new JSONObject();

                /*    for (int i = 0; i < sharedPreferences.getListProductos("productosPedidos", Producto.class).size(); i++)
                    {
                        if(sharedPreferences.getListProductos("productosPedidos",Producto.class).get(i) != null )
                        {
                            producto = new JSONObject();
                            nomProducto = new JSONObject();

                            try
                            {
                                producto.put("cantidad",sharedPreferences.getListProductos("productosPedidos", Producto.class).get(i).getNumeroDeProducto());
                                producto.put("valor",sharedPreferences.getListProductos("productosPedidos", Producto.class).get(i).getPrecioPideProducto());
                                nomProducto.put(sharedPreferences.getListProductos("productosPedidos", Producto.class).get(i).getNombreProducto(),producto);
                                productos.put(nomProducto);

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
                        pedido.put("pedido", productos);
                    }
                    catch (JSONException e)
                    {
                        e.printStackTrace();
                    }


                    String jsonStr = pedido.toString();
                    Log.i("JSON:",""+jsonStr);*/

                    Intent intent=new Intent(Pedidos.this, Compra.class);

                    startActivity(intent);
                }

            }
        });

        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle item selection
        switch (item.getItemId())
        {
            case R.id.ic_busqueda_producto:
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void mostrarDialogoDetalle(final int position, final String idProducto, final String imagenProducto, final String nombreProducto,
                                      final String cantProducto, final String precioGeneral, final String precioPide, final String ahorroProducto)
    {

        LayoutInflater inflater = getLayoutInflater();
        final View alertLayout = inflater.inflate(R.layout.layout_detalle_producto, null);
        final ImageView imagen_detalle_producto = (ImageView) alertLayout.findViewById(R.id.imagen_detalle_producto);
        final TextView contadorProductoSolicitados=(TextView) alertLayout.findViewById(R.id.contadorProducto_detalle);

        if (imagenProducto.equals("http://fasttrackcenter.com/pide/ws/images/"))
        {
            imagen_detalle_producto.setImageResource(R.drawable.ic_not_image_found);
        }

        else
        {
            Glide.with(this).
                    load(imagenProducto).
                    thumbnail(0.5f).into(imagen_detalle_producto);
        }

        final RatingBar ratingBarDetalleProducto = (RatingBar) alertLayout.findViewById(R.id.ratingBarDetalleProducto);
        final TextView nombre_producto_detalle = (TextView) alertLayout.findViewById(R.id.nombre_producto_detalle);
        nombre_producto_detalle.setText(nombreProducto);

        final TextView cantidad_producto_detalle = (TextView) alertLayout.findViewById(R.id.cantidad_producto_detalle);
        cantidad_producto_detalle.setText(cantProducto);

        final TextView precio_producto_detalle = (TextView) alertLayout.findViewById(R.id.precio_producto_detalle);
        precio_producto_detalle.setText("$"+numberFormat.format(Double.parseDouble(precioPide)));
        //CONTADORES PRODUCTOS
        final ImageView botonIncrementarProducto = (ImageView) alertLayout.findViewById(R.id.botonIncrementarProducto);
        final ImageView botonDecrementarProducto = (ImageView) alertLayout.findViewById(R.id.botonDecrementarProducto);

        final Button botonAgregarBolsa = (Button) alertLayout.findViewById(R.id.botonAgregarBolsa);

        final Button botonAgregarBolsaDeshabilitado = (Button) alertLayout.findViewById(R.id.botonAgregarBolsaDeshabilitado);

        final Button botonRemoverBolsa = (Button) alertLayout.findViewById(R.id.botonRemoverBolsa);
        final Button botonAjustarBolsa = (Button) alertLayout.findViewById(R.id.botonAjustarBolsa);
        //INCREMENTO DE PRODUCTOS
        botonIncrementarProducto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                contadorGeneral++;
                //updateHotCount(contadorGeneral);
                try
                {
                    if(sharedPreferences.getListProductos("productosPedidos",Producto.class).get(position) != null)
                    {
                        i+=Integer.parseInt(TextUtils.equals(sharedPreferences.getListProductos("productosPedidos",Producto.class).get(position).getNumeroDeProducto().toString(),null)?"0":
                                sharedPreferences.getListProductos("productosPedidos",Producto.class).get(position).getNumeroDeProducto().toString());

                        i=Integer.parseInt(contadorProductoSolicitados.getText().toString());



                    }


                }
                catch ( IndexOutOfBoundsException e )
                {
                    contadorProductoSolicitados.setText("0");
                }
                i++;
                contadorProductoSolicitados.setText(""+i);
                botonAgregarBolsa.setVisibility(View.VISIBLE);
                botonAgregarBolsaDeshabilitado.setVisibility(View.GONE);
                botonRemoverBolsa.setVisibility(View.GONE);
            }
        });

        botonDecrementarProducto.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

                contadorGeneral--;

                try
                {
                    //TRY CUANDO EXISTEN PRODUCTOS EN MEMORIA
                    if(sharedPreferences.getListProductos("productosPedidos",Producto.class).get(position) != null)
                    {

                        i=Integer.parseInt(contadorProductoSolicitados.getText().toString());
                        //i--;
                        Log.i("valor uno i",""+i);

                        if(i>0)
                        {
                            //i=Integer.parseInt(sharedPreferences.getListProductos("productosPedidos",Producto.class).get(position).getNumeroDeProducto());
                            i--;
                            Log.i("valor dos i",""+i);
                            sharedPreferences.getListProductos("productosPedidos",Producto.class).get(position).setNumeroDeProducto(""+i);
                        }



                        if(i==0 || contadorProductoSolicitados.getText().equals("0"))
                        {
                            contadorProductoSolicitados.setText("0");
                            //contadorGeneral=0;
                            botonAgregarBolsa.setVisibility(View.GONE);
                            botonAgregarBolsaDeshabilitado.setVisibility(View.VISIBLE);
                            //botonDecrementarProducto.setEnabled(false);
                            //updateHotCount(contadorGeneral);
                            botonAgregarBolsa.setVisibility(View.GONE);
                            botonAgregarBolsaDeshabilitado.setVisibility(View.GONE);
                            botonRemoverBolsa.setVisibility(View.VISIBLE);
                            sharedPreferences.getListProductos("productosPedidos",Producto.class).get(position).setNumeroDeProducto(""+i);
                            return;
                        }

                        //i=Integer.parseInt(String.valueOf(contadorProductoSolicitados.getText()));


                        Log.i("valor decremento i guardado",""+contadorProductoSolicitados.getText());


                    }
                }
                catch ( IndexOutOfBoundsException e )
                {
                    //TRY CUANDO LA MEMORIA ESTA VACIA
                    if(i>0)
                    {
                        i--;
                    }

                    Log.i("valor decremento i",""+i);
                    Log.i("valor decremento i",""+contadorProductoSolicitados.getText());
                    if(i==0 || contadorProductoSolicitados.getText().equals("0"))
                    {
                        contadorProductoSolicitados.setText("0");
                        contadorGeneral=0;
                        botonAgregarBolsa.setVisibility(View.GONE);
                        botonAgregarBolsaDeshabilitado.setVisibility(View.VISIBLE);
                        //botonDecrementarProducto.setEnabled(false);
                        //updateHotCount(contadorGeneral);
                        return;
                    }
                }


                botonAgregarBolsa.setVisibility(View.VISIBLE);
                botonAgregarBolsaDeshabilitado.setVisibility(View.GONE);


                contadorProductoSolicitados.setText(""+i);
                //updateHotCount(contadorGeneral);
            }
        });


         botonRemoverBolsa.setOnClickListener(new View.OnClickListener()
         {
             @Override
             public void onClick(View view)
             {

                 Toast.makeText(getApplicationContext(),"Contador General: "+contadorGeneral,Toast.LENGTH_LONG).show();
                 Toast.makeText(getApplicationContext(),"Contador Producto: "+sharedPreferences.getListProductos("productosPedidos",Producto.class).get(position).getNumeroDeProducto(),Toast.LENGTH_LONG).show();

                 //contadorGeneral=(contadorGeneral)-(Integer.parseInt(sharedPreferences.getListProductos("productosPedidos",Producto.class).get(position).getNumeroDeProducto()));
                 updateHotCount(contadorGeneral);
                 pruebaProductosPedidos[position]=null;
                 productosPedidos.remove(position);
                 sharedPreferences.putListProductos("productosPedidos",productosPedidos);
                /* sharedPreferences.getListProductos("productosPedidos",Producto.class).set(position,null);

                 updateHotCount((contadorGeneral)-(Integer.parseInt(sharedPreferences.getListProductos("productosPedidos",Producto.class).get(position).getNumeroDeProducto())));*/
                 alertDialogCompra.dismiss();
             }
         });


        botonAgregarBolsa.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Producto p=new Producto();

                p.setIdProducto(idProducto);
                p.setImagenProducto(imagenProducto);
                p.setNombreProducto(nombreProducto);
                p.setCantidadProducto(cantProducto);
                p.setPrecioGeneralProducto(precioGeneral);
                p.setPrecioPideProducto(precioPide);
                p.setAhorroProducto(ahorroProducto);
                //CANTIDAD SOLICITADA DEL PRODUCTO
                p.setNumeroDeProducto(contadorProductoSolicitados.getText().toString());
                //AGREGAMOS PRODUCTO SELECCIONADO A LA BOLSA
                pruebaProductosPedidos[position]=p;
                productosPedidos=new ArrayList<Producto>( Arrays.asList(pruebaProductosPedidos));
                sharedPreferences.putListProductos("productosPedidos",productosPedidos);
                Toast.makeText(getApplicationContext(),"cantidad: "+sharedPreferences.getListProductos("productosPedidos",Producto.class).get(position).getNumeroDeProducto(),Toast.LENGTH_LONG).show();

                updateHotCount(contadorGeneral);

                i=0;
                alertDialogCompra.dismiss();
            }
        });


        for(int i = 0; i < sharedPreferences.getListProductos("productosPedidos",Producto.class).size(); i++)
        {
            Log.i("arrays",i+": "+sharedPreferences.getListProductos("productosPedidos",Producto.class).get(i));

        }

        try
        {
            if(sharedPreferences.getListProductos("productosPedidos",Producto.class).get(position) != null)
            {
                contadorProductoSolicitados.setText(""+sharedPreferences.getListProductos("productosPedidos",Producto.class).get(position).getNumeroDeProducto());

            }
        }
        catch ( IndexOutOfBoundsException e )
        {
            contadorProductoSolicitados.setText("0");


        }

        //CUANDO CERRAMOS VENTANA, INICIALIZAMOS DE NUEVO EN CERO
        i=0;

        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setView(alertLayout);
        alert.setCancelable(true);
        alertDialogCompra = alert.create();
        //alertDialogCompra.getWindow().setBackgroundDrawable(new ColorDrawable(getColorWithAlpha(Color.BLACK,0.7f)));
        alertDialogCompra.show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id)
    {
        Producto producto = (Producto) parent.getItemAtPosition(position);

        mostrarDialogoDetalle(position,producto.getIdProducto(),producto.getImagenProducto(), producto.getNombreProducto(),
                producto.getCantidadProducto(), producto.getPrecioGeneralProducto(),producto.getPrecioPideProducto(),
                producto.getAhorroProducto());

    }

    private void WebServiceGetProductos()
    {
        listadoProductos.clear();
        String _urlWebService = vars.ipServer.concat("/ws/getProducto");

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
                                gridView.setVisibility(View.VISIBLE);
                                JSONArray listaProductos = response.getJSONArray("productos");
                                sizeListaProductos=listaProductos.length();
                                pruebaProductosPedidos= new Producto[sizeListaProductos];
                                showGrid(listaProductos);
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Pedidos.this,R.style.AlertDialogTheme));
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Pedidos.this,R.style.AlertDialogTheme));
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Pedidos.this,R.style.AlertDialogTheme));
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Pedidos.this,R.style.AlertDialogTheme));
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Pedidos.this,R.style.AlertDialogTheme));
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Pedidos.this,R.style.AlertDialogTheme));
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

                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Pedidos.this,R.style.AlertDialogTheme));
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
                headers.put("codEmpresa", codEmpresa);
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

}
