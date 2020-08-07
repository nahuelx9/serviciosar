package np.com.proyecto.servicio;

import np.com.proyecto.exception.FileStorageException;
import np.com.proyecto.exception.MyFileNotFoundException;
import np.com.proyecto.domain.DBFile;
import np.com.proyecto.repository.DBFileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@Service
public class DBFileStorageService implements DBFileService {

    @Autowired
    private DBFileRepository dbFileRepository;

    public DBFile storeFile(MultipartFile file) {
        // Normalize file name
        String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        try {
            // Check if the file's name contains invalid characters
            if(fileName.contains("..")) {
                throw new FileStorageException("Sorry! Filename contains invalid path sequence " + fileName);
            }

            DBFile dbFile = new DBFile(fileName, file.getContentType(), file.getBytes());
            return dbFile;
        } catch (IOException ex) {
            throw new FileStorageException("Could not store file " + fileName + ". Please try again!", ex);
        }
    }

    public DBFile getFile(String fileId) {
        return dbFileRepository.findById(fileId)
                .orElseThrow(() -> new MyFileNotFoundException("File not found with id " + fileId));
    }
    
    public void guardar(DBFile dbFile){
        dbFileRepository.save(dbFile);
    }

    @Override
    public List<DBFile> listarDBFiles() {
        return (List<DBFile>) dbFileRepository.findAll();
    }

    @Override
    public void eliminar(DBFile dbFile) {
        dbFileRepository.delete(dbFile);
    }

    @Override
    public DBFile encontrarDBFile(DBFile dbFile) {
        return  dbFileRepository.findById(dbFile.getId()).orElse(null);
    }



}
