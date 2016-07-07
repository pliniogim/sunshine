package com.example.android.sunshine.app;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by dti-053 on 01/07/2016.
 */

public class PlaceHolderFragment {
    /**
     * Classe PlaceHolderFragment
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        /**
         * private String[] forecastWeather = {
         * "Seg",
         * "Ter",
         * "Qua",
         * "Qui",
         * "Sex",
         * "Sab"
         * };
         */

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            /**
             * Transforma o layout XML em Java para podermos utilizar abaixo
             */
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            /**
             * inicializa o resource para criar um array de strings a partir de um arquivo
             */
            Resources res = getResources();
            String[] forecast = res.getStringArray(R.array.forecast_array);

            /**
             * PDG
             * List<String> list = Arrays.asList(array);
             * Definido desta forma, faz com que a lista fique com tamanho fixo, sem possibilidade de adicionar dados
             *
             * List<String> list = new ArrayList<String>(Arrays.asList(array);
             * Definido desta forma a nova lista terá comprimento variável podendo adicionar dados posteriormente
             */
            List<String> weekForecast = new ArrayList<>(Arrays.asList(forecast));

            /**
             * PDG
             * O adaptador definido para mostrar a lista: ArrayAdapter
             * ArrayAdapter<String>(context, layout, TextView, array)
             * @param context pega a atividade definida pelo fragmento
             * @param layout o layout que contém a ListView
             * @param TextView o TextView que será mostrado para cada item
             * @param Array a base dos dados que precisamos mostrar
             *
             */
            ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.list_item_forecast, R.id.list_item_forecast_textview, weekForecast);

            /**
             * Encontra o id do ListView e seta o adaptador para o que definimos
             * a partir de uma busca na árvore abaixo de rootView definido acima (diminui o tempo de busca)
             */
            ListView listView = (ListView) rootView.findViewById(R.id.listview_forecast);
            listView.setAdapter(adapter);

            // These two need to be declared outside the try/catch
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast
                URL url = new URL("http://api.openweathermap.org/data/2.5/forecast/daily?id=3448439&mode=json&units=metric&cnt=5&apikey=cb9c900d6f96c04118d9899978b45149");

                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e("PlaceholderFragment", "Error ", e);
                // If the code didn't successfully get the weather data, there's no point in attemping
                // to parse it.
                return null;
            } finally {
                //Log.i("BUFFER",forecastJsonStr);
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("PlaceholderFragment", "Error closing stream", e);
                    }
                }
            }
            return rootView;
        }
    }

}
