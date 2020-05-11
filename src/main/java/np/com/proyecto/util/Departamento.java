
package np.com.proyecto.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONObject;


public class Departamento extends Provincia {
    
    private int id;
    private String nombre;

    public Departamento() {
    }

    public Departamento(int id, String nombre) {
        super(id, nombre);
    }


        
     public List<Departamento> listarDepartamento() {
        List<Departamento> DepartamentoListM = new ArrayList<Departamento>();
        try {
            //creamos una URL donde esta nuestro webservice
            URL url = new URL("https://apis.datos.gob.ar/georef/api/municipios?campos=provincia.id,nombre&max=1814");
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            //indicamos por que verbo HTML ejecutaremos la solicitud
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Accept", "application/json");
            if (conn.getResponseCode() != 200) {
                //si la respuesta del servidor es distinta al codigo 200 lanzaremos una Exception
                throw new RuntimeException("Failed : HTTP error code : " + conn.getResponseCode());
            }
            BufferedReader br = new BufferedReader(new InputStreamReader((conn.getInputStream())));
            //creamos un StringBuilder para almacenar la respuesta del web service
            StringBuilder sb = new StringBuilder();
            int cp;
            while ((cp = br.read()) != -1) {
                sb.append((char) cp);
            }
            //en la cadena output almacenamos toda la respuesta del servidor
            String output = sb.toString();
            //convertimos la cadena a JSON a traves de la libreria GSON
            JSONObject json = new JSONObject(output);
            JSONArray objA = json.getJSONArray("municipios");
            for (int i = 0; i < objA.length(); i++) {
                JSONObject elemento = objA.getJSONObject(i);
                int id = elemento.getJSONObject("provincia").getInt("id");
                String nombre = elemento.getString("nombre");
                Departamento departamento = new Departamento(id, nombre);
                DepartamentoListM.add(departamento);
            }
            conn.disconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return DepartamentoListM;
    }
}
