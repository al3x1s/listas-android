/*
 * Copyright 2016 Irving Gonzalez (ialexis93@gmail.com)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package info.tinyservice.listas.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.MultiAutoCompleteTextView;
import android.widget.Toast;

import info.tinyservice.listas.MainApplication;
import info.tinyservice.listas.R;

public class SendListadoDialog extends DialogFragment {
    private MainApplication application;
    private DialogFragment dialog;

    private OnAddSendListadoListener callback;
    public interface OnAddSendListadoListener {
        void onAddSendListadoSubmit(String[] emailTo);
    }

    private MultiAutoCompleteTextView mt;
    private Button sendListado, cancelSend;


    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        application = (MainApplication) getActivity().getApplication();
        dialog = this;

        try {
            callback = (OnAddSendListadoListener) getTargetFragment();
        } catch (ClassCastException e) {
            throw new ClassCastException("Calling Fragment must implement OnAddPersonalListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.dialog_sendlistado, container, false);

        mt = (MultiAutoCompleteTextView) v.findViewById(R.id.mactv);
        sendListado = (Button) v.findViewById(R.id.sendButton);
        cancelSend = (Button) v.findViewById(R.id.cancelSendListado);

        String[] availableEmails = application.getDataManager().getAllDestinatarios();

        mt.setTokenizer(new MultiAutoCompleteTextView.CommaTokenizer());
        ArrayAdapter<String> adp=new ArrayAdapter<String>(getActivity(), android.R.layout.simple_dropdown_item_1line, availableEmails);
        mt.setThreshold(1);
        mt.setAdapter(adp);

        sendListado.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String[] emailTo = buildEmailArray(mt.getText().toString());
                if(emailTo.length > 0){
                    callback.onAddSendListadoSubmit(emailTo);
                    dialog.dismiss();
                }else{
                    Toast.makeText(getContext(), "Ingrese un correo electronico", Toast.LENGTH_SHORT).show();
                }
            }
        });

        cancelSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        return v;
    }


    private String[] buildEmailArray(String rawEmail){
        if(rawEmail != null && rawEmail.length() >0) {
            return rawEmail.split(",");
        }else{
            return null;
        }
    }
}
