/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package np.com.proyecto.dao;

import np.com.proyecto.domain.Alerta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AlertaDao extends JpaRepository<Alerta, Integer> {
    
}
