/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package np.com.proyecto.servicio;

import java.util.List;
import np.com.proyecto.dao.BuzonDao;
import np.com.proyecto.domain.Buzon;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BuzonServiceImpl implements BuzonService {
    
    @Autowired
    private BuzonDao buzonDao;

    @Override
    public List<Buzon> listarBuzon() {
           return (List<Buzon>) buzonDao.findAll();
    }

    @Override
    public void guardar(Buzon buzon) {
        buzonDao.save(buzon);
    }

    @Override
    public void eliminar(Buzon buzon) {
      buzonDao.delete(buzon);
    }

    @Override
    @Transactional
    public Buzon encontrarBuzon(Buzon buzon) {
      return buzonDao.findById(buzon.getIdBuzon()).orElse(null);
    }


}
