package com.teoriagrafos.athena.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.teoriagrafos.athena.R;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class ExpandableAdapter extends BaseExpandableListAdapter {

    // Cria variáveis para gerenciar o ExpandableListView.
    private List<String> listGroup;
    private HashMap<String, List<String>> listData;
    private LayoutInflater inflater;

    // Cria uma classe ViewHolder para o grupo.
    class ViewHolderGroup{
        TextView infoGroup;
    }

    // Cria uma classe ViewHolder para os itens.
    class ViewHolderItem{
        TextView infoItem;
    }

    public ExpandableAdapter(Context context, List<String> listGroup, HashMap<String, List<String>> listData){
        this.listGroup = listGroup;
        this.listData = listData;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    // Retorna o tamanho do group.
    @Override
    public int getGroupCount() {
        return listGroup.size();
    }

    // Retorna quantidade de filhos no grupo.
    @Override
    public int getChildrenCount(int groupPosition) {
        return Objects.requireNonNull(listData.get(listGroup.get(groupPosition))).size();
    }

    // Retorna a posição grupo.
    @Override
    public Object getGroup(int groupPosition) {
        return listGroup.get(groupPosition);
    }

    // Retorna a posição do filho.
    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return Objects.requireNonNull(listData.get(listGroup.get(groupPosition))).get(childPosition);
    }

    // Retorna o id do grupo.
    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    // Retorna o id do filho.
    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    // Verifica se tem ids estáveis.
    @Override
    public boolean hasStableIds() {
        return false;
    }


    // Retorna o grupo da view.
    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        ViewHolderGroup viewHolderGroup;

        if (convertView == null){
            convertView = inflater.inflate(R.layout.header_expandable_list_view, parent, false);
            viewHolderGroup = new ViewHolderGroup();
            convertView.setTag(viewHolderGroup);

            viewHolderGroup.infoGroup = convertView.findViewById(R.id.infoGroup);
        } else {
            viewHolderGroup = (ViewHolderGroup) convertView.getTag();
        }

        viewHolderGroup.infoGroup.setText(listGroup.get(groupPosition));

        return convertView;
    }

    // Retorna o filho da view.
    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        ViewHolderItem viewHolderItem;
        String val = (String) getChild(groupPosition, childPosition);

        if (convertView == null){
            convertView = inflater.inflate(R.layout.item_expandable_list_view, parent, false);
            viewHolderItem = new ViewHolderItem();
            convertView.setTag(viewHolderItem);

            viewHolderItem.infoItem = convertView.findViewById(R.id.infoItem);
        } else {
            viewHolderItem = (ViewHolderItem) convertView.getTag();
        }

        viewHolderItem.infoItem.setText(val);

        return convertView;
    }

    // Retorna true se o filho for selecionado.
    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
