package com.ingeniapps.pide.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.ingeniapps.pide.R;
import com.ingeniapps.pide.beans.Empresa;

import java.util.ArrayList;

/**
 * Created by FABiO on 16/12/2016.
 */

public class EmpresaAdapter extends BaseAdapter
{
    private Context context;
    private ArrayList<Empresa> listadoEmpresas;

    public EmpresaAdapter(Context context, ArrayList<Empresa> listadoEmpresas)
    {
        this.context = context;
        this.listadoEmpresas = listadoEmpresas;
    }

    @Override
    public int getCount()
    {
        return listadoEmpresas.size();
    }

    @Override
    public Empresa getItem(int position)
    {
        return listadoEmpresas.get(position);
    }

    @Override
    public long getItemId(int position)
    {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup viewGroup)
    {

        if (view == null)
        {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.layout_item_empresa, viewGroup, false);
        }

        TextView nombreEmpresa = (TextView) view.findViewById(R.id.nombre_empresa);
        ImageView imagenEmpresa = (ImageView) view.findViewById(R.id.imagen_empresa);

        final Empresa empresa = getItem(position);

        nombreEmpresa.setText(empresa.getNomEmpresa());

        Glide.with(context)
                .load(empresa.getImaEmpresa())
                .into(imagenEmpresa);

        return view;
    }

}
