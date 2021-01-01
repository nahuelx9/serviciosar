package np.com.proyecto.util;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Id;
import org.json.JSONArray;
import org.json.JSONObject;

public class Provincia {

    @Id
    private int id;
    private String nombre;

    public Provincia() {
    }

    public Provincia(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Provincia> listarProvincia() {
        List<Provincia> provinciasListM = new ArrayList<Provincia>();
        try {
            //creamos una URL donde esta nuestro webservice
            URL url = new URL("https://apis.datos.gob.ar/georef/api/provincias?campos=id,nombre");
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
            JSONArray objA = json.getJSONArray("provincias");
            for (int i = 0; i < objA.length(); i++) {
                JSONObject elemento = objA.getJSONObject(i);
                int id = elemento.getInt("id");
                String nombreIso = elemento.getString("nombre");
                //String nombreUtf = new String(nombreIso.getBytes("ISO-8859-1"), "UTF-8");
                Provincia provincias = new Provincia(id, nombreIso);
                provinciasListM.add(provincias);
            }
            conn.disconnect();
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return provinciasListM;
    }
}
