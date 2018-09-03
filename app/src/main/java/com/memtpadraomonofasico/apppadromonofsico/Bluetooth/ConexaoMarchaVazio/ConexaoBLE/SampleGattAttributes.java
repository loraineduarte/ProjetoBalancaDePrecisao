/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.memtpadraomonofasico.apppadromonofsico.Bluetooth.ConexaoMarchaVazio.ConexaoBLE;

import java.util.HashMap;

/**
 * This class includes a small subset of standard GATT attributes for demonstration purposes.
 */
public class SampleGattAttributes {

    //Serviço 1 - Atributos Genéricos
    public static final String ATRIBUTOS_GENERICOS = "00001801-0000-1000-8000-00805f9b34fb";
    //características
    public static final String S1_SERVICE_CHANGED = "00002a05-0000-1000-8000-00805f9b34fb";

    //Serviço 2 - Acesso Generico
    public static final String ACESSO_GENERICO = "00001800-0000-1000-8000-00805f9b34fb";
    //características
    public static final String S2_DEVICE_NAME = "00002a00-0000-1000-8000-00805f9b34fb";
    public static final String S2_APPEARANCE = "00002a01-0000-1000-8000-00805f9b34fb";
    public static final String S2_PHERIPHERAL_PREFERRED_CONNECTION_PARAMETERS = "00002a04-0000-1000-8000-00805f9b34fb";

    //Serviço 3 - ?
    public static final String SERVICO_DO_PADRAO = "0003cdd0-0000-1000-8000-00805f9b0131";
    //características
    public static final String S3_1 = "0003cdd1-0000-1000-8000-00805f9b0131";
    public static final String S3_2 = "0003cdd2-0000-1000-8000-00805f9b0131";



    private static final HashMap<String, String> attributes = new HashMap();

    static {
        // Sample Services.
        attributes.put(ATRIBUTOS_GENERICOS, "Atributos Genericos");
        attributes.put(ACESSO_GENERICO, "Informações do Acesso");
        attributes.put(SERVICO_DO_PADRAO, "Serviço do Padrão ");

        // Sample Characteristics.
        attributes.put(S1_SERVICE_CHANGED, "Service Changed");
        attributes.put(S2_DEVICE_NAME, "Nome do Dispositivo");
        attributes.put(S2_APPEARANCE, "Appearance");
        attributes.put(S2_PHERIPHERAL_PREFERRED_CONNECTION_PARAMETERS, "PHERIPHERAL_PREFERRED_CONNECTION_PARAMETERS");
        attributes.put(S3_1, "S3_1");
        attributes.put(S3_2, "S3_2");
    }

    public static String lookup(String uuid, String defaultName) {
        String name = attributes.get(uuid);
        return name == null ? defaultName : name;
    }
}
