package com.ingeniapps.pide.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.ingeniapps.pide.R;

public class Login extends AppCompatActivity
{
    TextView textViewRegistroUsuario,textViewRecordarClaveLogin;
    TextView editTextEmail;
    EditText email;
    EditText clave;
    //public vars vars;
    private String emailUsuario,claveUsuario,MyToken,tokenFCM;
    private Button botonLogin;
    //gestionSharedPreferences gestionSharedPreferences;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;
    private ProgressDialog progressDialog;

    private Boolean guardarSesion;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        /*vars=new vars();
        gestionSharedPreferences=new gestionSharedPreferences(this);*/

        //COMPROBAMOS LA SESION DEL USUARIO
       /* guardarSesion=gestionSharedPreferences.getBoolean("GuardarSesion");
        if (guardarSesion==true)
        {
            cargarActivityPrincipal();
        }*/

        //EVENTOS INTERFAZ
        textViewRegistroUsuario=(TextView) findViewById(R.id.textViewRegistroLogin);
        textViewRegistroUsuario.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                /*Intent intent = new Intent(Login.this, Registro.class);
                startActivity(intent);*/
            }
        });

      /*  textViewRecordarClaveLogin = (TextView) findViewById(R.id.textViewRecordarClaveLogin);
        textViewRecordarClaveLogin.setOnClickListener(new View.OnClickListener()
        {
            public void onClick(View v)
            {
                Intent intent = new Intent(Login.this, ValidarEmail.class);
                startActivity(intent);
            }
        });*/

        email=(EditText)findViewById(R.id.email);
        clave=(EditText)findViewById(R.id.clave);

        botonLogin=(Button)findViewById(R.id.buttonIniciarSesion);
        botonLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                emailUsuario=email.getText().toString();
                claveUsuario=clave.getText().toString();

              /*  if (TextUtils.isEmpty(emailUsuario)||!(isValidEmail(emailUsuario)))
                {
                    email.setError("Digite un email valido");
                    email.requestFocus();
                    return;
                }

                if (TextUtils.isEmpty(claveUsuario))
                {
                    clave.setError("Digite su clave");
                    clave.requestFocus();
                    return;
                }*/

                //WebServiceLogin();
                Intent intent= new Intent(Login.this, Principal.class);
                startActivity(intent);
            }
        });

        if(checkPlayServices())
        {
            //revisar que de primerazo obtenga tokenFCM
            //tokenFCM=FirebaseInstanceId.getInstance().getToken();
            /*gestionSharedPreferences.putString("tokenFCM",FirebaseInstanceId.getInstance().getToken());
            Toast.makeText(this,"Token FCM: "+gestionSharedPreferences.getString("tokenFCM"), Toast.LENGTH_LONG).show();*/
            //Toast.makeText(this,"Token FCM: "+ tokenFCM,Toast.LENGTH_LONG).show();
        }
        else
        {
            /*AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Login.this,R.style.AlertDialogTheme));
            builder
                    .setTitle("GOOGLE PLAY SERVICES")
                    .setMessage("Se ha encontrado un error con los servicios de Google Play, actualizalo y vuelve a ingresar.")
                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener()
                    {
                        @Override
                        public void onClick(DialogInterface dialog, int id)
                        {
                            finish();
                        }
                    }).setCancelable(false).show().getButton(DialogInterface.BUTTON_POSITIVE).
                    setTextColor(getResources().getColor(R.color.colorPrimary));*/
        }
    }

    public final static boolean isValidEmail(CharSequence target)
    {
        if (target == null)
        {
            return false;
        }
        else
        {
            return android.util.Patterns.EMAIL_ADDRESS.matcher(target).matches();
        }
    }

    public void cargarActivityPrincipal()
    {
        /*Intent intent = new Intent(Login.this, Principal.class);
        startActivity(intent);
        Login.this.finish();*/
    }

    private boolean checkPlayServices()
    {
        GoogleApiAvailability googleAPI = GoogleApiAvailability.getInstance();
        int result = googleAPI.isGooglePlayServicesAvailable(this);
        if(result != ConnectionResult.SUCCESS)
        {
            if(googleAPI.isUserResolvableError(result))
            {
                googleAPI.getErrorDialog(this, result,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            }

            return false;
        }

        return true;
    }

   /* private void WebServiceLogin()
    {
        String _urlWebService=vars.ipServer.concat("/ws/Login");

        progressDialog = new ProgressDialog(new ContextThemeWrapper(Login.this,R.style.AppCompatAlertDialogStyle));
        progressDialog.setIndeterminate(true);
        progressDialog.setMessage("Validando el usuario, por favor espere...");
        progressDialog.show();
        progressDialog.setCancelable(false);

        JsonObjectRequest jsonObjReq=new JsonObjectRequest(Request.Method.GET, _urlWebService, null,
                new Response.Listener<JSONObject>()
                {
                    @Override
                    public void onResponse(JSONObject response)
                    {
                        try
                        {
                            boolean status=response.getBoolean("status");
                            String message=response.getString("message");

                            if(status)
                            {
                                //OBTENEMOS DATOS DEL USUARIO PARA GUARDAR SU SESION
                                gestionSharedPreferences.putBoolean("GuardarSesion", true);
                                gestionSharedPreferences.putString("nomUsuario",""+response.getString("nomUsuario"));
                                gestionSharedPreferences.putString("emaUsuario",""+response.getString("emaUsuario"));
                                gestionSharedPreferences.putString("MyToken",""+response.getString("MyToken"));
                                gestionSharedPreferences.putString("codUsuario",""+response.getString("codUsuario"));

                                Intent intent=new Intent(Login.this,Principal.class);
                                startActivity(intent);
                                finish();
                                progressDialog.dismiss();
                            }
                            else
                            {
                                progressDialog.dismiss();
                                AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Login.this,R.style.AlertDialogTheme));
                                builder
                                        .setTitle("ESTADO VERIFICACIÓN")
                                        .setMessage(message)
                                        .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener()
                                        {
                                            @Override
                                            public void onClick(DialogInterface dialog, int id)
                                            {

                                            }
                                        }).setCancelable(false).show().getButton(DialogInterface.BUTTON_POSITIVE).
                                        setTextColor(getResources().getColor(R.color.colorPrimary));
                            }
                        }
                        catch (JSONException e)
                        {
                            progressDialog.dismiss();
                            AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Login.this,R.style.AlertDialogTheme));
                            builder
                                    .setTitle("ESTADO PROCESO")
                                    .setMessage(e.getMessage().toString())
                                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                        }
                                    }).setCancelable(false).show().getButton(DialogInterface.BUTTON_POSITIVE).
                                    setTextColor(getResources().getColor(R.color.colorPrimary));
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener()
                {
                    @Override
                    public void onErrorResponse(VolleyError error)
                    {
                        progressDialog.dismiss();
                        AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(Login.this,R.style.AlertDialogTheme));
                        builder
                                .setTitle("ESTADO PROCESO")
                                .setMessage(error.toString())
                                .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener()
                                {
                                    @Override
                                    public void onClick(DialogInterface dialog, int id)
                                    {
                                    }
                                }).setCancelable(false).show().getButton(DialogInterface.BUTTON_POSITIVE).
                                setTextColor(getResources().getColor(R.color.colorPrimary));

                        if (error instanceof TimeoutError)
                        {
                            progressDialog.dismiss();
                            builder
                                    .setTitle("ESTADO PROCESO")
                                    .setMessage(error.toString())
                                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                        }
                                    }).setCancelable(false).show().getButton(DialogInterface.BUTTON_POSITIVE).
                                    setTextColor(getResources().getColor(R.color.colorPrimary));
                        }
                        else
                        if (error instanceof NoConnectionError)
                        {
                            progressDialog.dismiss();
                            builder
                                    .setTitle("ESTADO PROCESO")
                                    .setMessage(error.toString())
                                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                        }
                                    }).setCancelable(false).show().getButton(DialogInterface.BUTTON_POSITIVE).
                                    setTextColor(getResources().getColor(R.color.colorPrimary));                        }

                        else

                        if (error instanceof AuthFailureError)
                        {
                            progressDialog.dismiss();
                            builder
                                    .setTitle("ESTADO PROCESO")
                                    .setMessage(error.toString())
                                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                        }
                                    }).setCancelable(false).show().getButton(DialogInterface.BUTTON_POSITIVE).
                                    setTextColor(getResources().getColor(R.color.colorPrimary));
                        }

                        else

                        if (error instanceof ServerError)
                        {
                            progressDialog.dismiss();
                            builder
                                    .setTitle("ESTADO REGISTRO")
                                    .setMessage(error.toString())
                                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                        }
                                    }).setCancelable(false).show().getButton(DialogInterface.BUTTON_POSITIVE).
                                    setTextColor(getResources().getColor(R.color.colorPrimary));
                        }
                        else
                        if (error instanceof NetworkError)
                        {
                            progressDialog.dismiss();
                            builder
                                    .setTitle("ESTADO PROCESO")
                                    .setMessage(error.toString())
                                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                        }
                                    }).setCancelable(false).show().getButton(DialogInterface.BUTTON_POSITIVE).
                                    setTextColor(getResources().getColor(R.color.colorPrimary));
                        }
                        else
                        if (error instanceof ParseError)
                        {
                            progressDialog.dismiss();
                            builder
                                    .setTitle("ESTADO PROCESO")
                                    .setMessage(error.toString())
                                    .setPositiveButton("ACEPTAR", new DialogInterface.OnClickListener()
                                    {
                                        @Override
                                        public void onClick(DialogInterface dialog, int id)
                                        {
                                        }
                                    }).setCancelable(false).show().getButton(DialogInterface.BUTTON_POSITIVE).
                                    setTextColor(getResources().getColor(R.color.colorPrimary));
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
                headers.put("user", emailUsuario);
                headers.put("pass", claveUsuario);
                headers.put("tokenFCM", tokenFCM);
                return headers;
            }
        };

        ControllerSingleton.getInstance().addToReqQueue(jsonObjReq, "");
        jsonObjReq.setRetryPolicy(new DefaultRetryPolicy(20000, 1, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
    }*/
}
