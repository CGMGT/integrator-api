package gt.com.tigo.integradorhome.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import gt.com.tigo.integradorhome.dao.ApplicationParameterRepository;
import gt.com.tigo.integradorhome.dto.DataTableRequestDto;
import gt.com.tigo.integradorhome.dto.PaginatedDataDto;
import gt.com.tigo.integradorhome.dto.ResourceDto;
import gt.com.tigo.integradorhome.model.AdmUsuarioEntity;
import gt.com.tigo.integradorhome.util.exception.*;
import gt.com.tigo.integradorhome.dao.UserRepository;
import gt.com.tigo.integradorhome.dto.FilterInfoDto;
import gt.com.tigo.integradorhome.util.SpecFactory;
import gt.com.tigo.integradorhome.util.service.AbstractService;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.client.methods.RequestBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import javax.xml.bind.DatatypeConverter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Timestamp;
import java.util.*;

import static gt.com.tigo.integradorhome.util.SpecFactory.*;

@Service
public class UserService extends AbstractService<AdmUsuarioEntity, UserRepository> {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserService.class);
    public static final String NO_SE_HA_PODIDO_OBTENER_UNA_RESPUESTA_DEL_SERVIDOR = "No se ha podido obtener una respuesta del servidor.";
    public static final String SE_HA_OBTENIDO_UNA_RESPUESTA_INVALIDA_DEL_SERVIDOR = "Se ha obtenido una respuesta inválida del servidor.";
    public static final String USUARIO = "usuario";
    public static final String NOMBRES = "nombres";
    public static final String APELLIDOS = "apellidos";
    public static final String CORREO_ELECTRONICO = "correoElectronico";
    public static final String PUESTO = "puesto";
    public static final String NOMBRE_MOSTRADO = "nombreMostrado";
    public static final String DISPLAY_NAME = "displayName";
    public static final String BUSINESS_PHONES = "businessPhones";
    public static final String OFFICE_LOCATION = "officeLocation";
    public static final String PREFERRED_LANGUAGE = "preferredLanguage";
    public static final String JOB_TITLE = "jobTitle";
    public static final String MOBILE_PHONE = "mobilePhone";
    public static final String SURNAME = "surname";
    public static final String MAIL = "mail";
    public static final String GIVEN_NAME = "givenName";
    public static final String ID = "id";


    @Autowired
    private UserRepository userRepo;

    @Autowired
    private GroupService groupService;

    @Autowired
    private ApplicationParameterRepository applicationParameterRepository;

    /**
     * Returns all resources' data.
     * @param token Azure AD token to access the Azure AD services.
     * @return a map containing all resources' data.
     * @throws Exception when an error occurs.
     */
    public Map<String, Object> getInfo(String token) throws SearchIdentityException {
        LOGGER.debug(String.format("@%s::getInfo(%s)", this.getClass().getName(), token));

        HttpClient client = HttpClients.custom().build();

        HttpUriRequest request = RequestBuilder.get()
                .setUri("https://graph.microsoft.com/v1.0/me")
                .setHeader(HttpHeaders.AUTHORIZATION, token)
                .build();

        HttpResponse response;

        try {
            response = client.execute(request);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());

            throw new SearchIdentityException(NO_SE_HA_PODIDO_OBTENER_UNA_RESPUESTA_DEL_SERVIDOR);
        }

        if (response.getStatusLine().getStatusCode() == 200) {
            String body;

            try {
                body = EntityUtils.toString(response.getEntity());
            } catch (IOException | ParseException ex) {
                LOGGER.error(ex.getMessage());

                throw new SearchIdentityException(SE_HA_OBTENIDO_UNA_RESPUESTA_INVALIDA_DEL_SERVIDOR);
            }

            ObjectMapper mapper = new ObjectMapper();

            try {
                return mapper.readValue(body, Map.class);
            } catch (IOException ex) {
                LOGGER.error(ex.getMessage());

                throw new SearchIdentityException("No se han podido leer los datos del usuario.");
            }
        } else {
            throw new SearchIdentityException(NO_SE_HA_PODIDO_OBTENER_UNA_RESPUESTA_DEL_SERVIDOR);
        }
    }

    /**
     * Returns a string containing the image bytes.
     * @param token Azure AD token to access the Azure AD services.
     * @return a string containing the image bytes in code base 64.
     * @throws Exception when an error occurs.
     */
    public String getPhoto(String token) throws SearchIdentityException {
        LOGGER.debug(String.format("@%s::getPhoto(%s)", this.getClass().getName(), token));

        HttpClient client = HttpClients.custom().build();

        HttpUriRequest request = RequestBuilder.get()
                .setUri("https://graph.microsoft.com/v1.0/me/photo/$value")
                .setHeader(HttpHeaders.AUTHORIZATION, token)
                .build();

        HttpResponse response;

        try {
            response = client.execute(request);
        } catch (IOException ex) {
            LOGGER.error(ex.getMessage());

            throw new SearchIdentityException(NO_SE_HA_PODIDO_OBTENER_UNA_RESPUESTA_DEL_SERVIDOR);
        }

        if (response.getStatusLine().getStatusCode() == 404) {
            throw new SearchIdentityException("El usuario no posee foto.");
        } else {
            String encodedString;

            try {
                InputStream is = response.getEntity().getContent();

                ByteArrayOutputStream bos = new ByteArrayOutputStream();

                int b;

                while ((b = is.read()) != -1) {
                    bos.write(b);
                }

                is.close();

                bos.flush();

                encodedString = DatatypeConverter.printBase64Binary(bos.toByteArray());

                bos.close();
            } catch (IOException ex) {
                LOGGER.error(ex.getMessage());

                throw new SearchIdentityException("No se han podido obtener los bytes de la foto del usuario.");
            }

            return encodedString;
        }
    }

    /**
     * Returns a resource by uid.
     * @param token Azure AD token to access the Azure AD services.
     * @return an object containing the found resource.
     * @throws ResourceNotFoundException when no record is found.
     */
    public AdmUsuarioEntity findByUid(String token) throws ResourceNotFoundException, ResourceCreateException, ResourceUpdateException {
        LOGGER.debug(String.format("@%s::findByUid(********)", this.getClass().getName()));

        Map<String, Object> userInfo = null;
        try {
            userInfo = this.getInfo(token);
        } catch (Exception e) {
            throw new ResourceNotFoundException();
        }

        if (userInfo == null || userInfo.size() <= 0 || !userInfo.containsKey(ID)) {
            throw new ResourceNotFoundException();
        }

        String photo = null;

        try {
            photo = this.getPhoto(token);
        } catch (Exception ex) {
            LOGGER.error("Couldn't get user photo.");
        }

        AdmUsuarioEntity user = null;

        try {
            LOGGER.debug(String.format("Searching for user with email %s", userInfo.get(MAIL).toString()));
            user = this.userRepo.findByCorreoElectronico(userInfo.get(MAIL).toString().toLowerCase());
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());
            throw new ResourceNotFoundException();
        }

        // if user does not exist then create
        if (user == null) {
            try {
                user = new AdmUsuarioEntity();
                user = this.fillUser(user, userInfo);
                user.setActivo(true);
                user.setFoto(photo);
                user.setFechaCreacion(new Timestamp(new Date().getTime()));
                user.setUsuarioCreacion("PROCESO");
                user.setToken(createJWT("Tigo", "OrquestadorFS", "JWT Token", 86400)); // 24 hours

                user = this.userRepo.save(user);
            } catch (Exception ex) {
                throw new ResourceCreateException();
            }
        } else {
            try {
                user = this.fillUser(user, userInfo);
                user.setFoto(photo);
                user.setFechaModificacion(new Timestamp(new Date().getTime()));
                user.setUsuarioModificacion("PROCESO");
                user.setToken(createJWT("Tigo", "OrquestadorFS", "JWT Token", 86400)); // 24 hours

                user = this.userRepo.save(user);
            } catch (Exception ex) {
                throw new ResourceUpdateException();
            }
        }

        return user;
    }

    private AdmUsuarioEntity fillUser(AdmUsuarioEntity user, Map<String, Object> userInfo) {
        String id = userInfo.containsKey(ID) ? userInfo.get(ID).toString() : null;
        String usuario = userInfo.containsKey(MAIL)
                ? userInfo.get(MAIL) == null
                    ? null
                    : userInfo.get(MAIL).toString()
                : null;
        String nombres = userInfo.containsKey(GIVEN_NAME)
                ? userInfo.get(GIVEN_NAME) == null
                    ? null
                    : userInfo.get(GIVEN_NAME).toString()
                : null;
        String apellidos = userInfo.containsKey(SURNAME)
                ? userInfo.get(SURNAME) == null
                    ? null
                    : userInfo.get(SURNAME).toString()
                : null;
        String correoElectronico = userInfo.containsKey(MAIL)
                ? userInfo.get(MAIL) == null
                    ? null
                    : userInfo.get(MAIL).toString().toLowerCase()
                : null;
        String nombreMostrado = userInfo.containsKey(DISPLAY_NAME)
                ? userInfo.get(DISPLAY_NAME) == null
                    ? null
                    : userInfo.get(DISPLAY_NAME).toString()
                : null;
        String puesto = userInfo.containsKey(JOB_TITLE)
                ? userInfo.get(JOB_TITLE) == null
                    ? null
                    : userInfo.get(JOB_TITLE).toString()
                : null;
        String telefonoCelular = userInfo.containsKey(MOBILE_PHONE)
                ? userInfo.get(MOBILE_PHONE) == null
                    ? null
                    : userInfo.get(MOBILE_PHONE).toString()
                : null;
        String telefonoOficina = userInfo.containsKey(BUSINESS_PHONES)
                ? userInfo.get(BUSINESS_PHONES) == null
                    ? null
                    : userInfo.get(BUSINESS_PHONES).toString()
                : null;
        String ubicacion = userInfo.containsKey(OFFICE_LOCATION)
                ? userInfo.get(OFFICE_LOCATION) == null
                    ? null
                    : userInfo.get(OFFICE_LOCATION).toString()
                : null;
        String idiomaPreferido = userInfo.containsKey(PREFERRED_LANGUAGE)
                ? userInfo.get(PREFERRED_LANGUAGE) == null
                    ? null
                    : userInfo.get(PREFERRED_LANGUAGE).toString()
                : null;

        user.setUuid(id);
        user.setUsuario(usuario);
        user.setNombres(nombres);
        user.setApellidos(apellidos);
        user.setCorreoElectronico(correoElectronico);
        user.setNombreMostrado(nombreMostrado);
        user.setPuesto(puesto);
        user.setTelefonoCelular(telefonoCelular);
        user.setTelefonoOficina(telefonoOficina);
        user.setUbicacion(ubicacion);
        user.setIdiomaPreferido(idiomaPreferido);

        return user;
    }

    private String createJWT(String id, String issuer, String subject, long ttlSec) {

        Long now = System.currentTimeMillis();

        String token = Jwts.builder()
                .setSubject(subject)
                // Convert to list of strings.
                // This is important because it affects the way we get them back in the Gateway.
                .setIssuer(issuer)
                .setId(id)
                .setIssuedAt(new Date(now))
                .setExpiration(new Date(now + ttlSec * 1000)) // in milliseconds
                .signWith(SignatureAlgorithm.HS512, "7!G0".getBytes())
                .compact();

        return "Bearer " + token;
    }

    /**
     * Returns a resource by email.
     * @param email The email of the requested resource
     * @return an object containing the found resource.
     * @throws ResourceNotFoundException when no record is found.
     */
    public AdmUsuarioEntity findByEmail(String email) throws ResourceNotFoundException {
        LOGGER.debug(String.format("@%s::findByEmail(%s)", this.getClass().getName(), email));

        AdmUsuarioEntity user = this.userRepo.findByCorreoElectronico(email);

        if (user == null) {
            throw new ResourceNotFoundException();
        }

        return user;
    }

    @Override
    public List<AdmUsuarioEntity> findAll() throws ResourcesNotFoundException {
        LOGGER.debug(String.format("@%s::findAll()", this.getClass().getName()));

        return super._findAll("id");
    }

    @Override
    public AdmUsuarioEntity findById(Long id) throws ResourceNotFoundException {
        LOGGER.debug(String.format("@%s::findById(%d)", this.getClass().getName(), id));

        return super._findById(id);
    }

    @Override
    protected Specification buildFilterSpecification(DataTableRequestDto dataTableRequestDto) throws InvalidFilterException {
        LOGGER.debug(String.format("@%s::buildFilterSpecification(%s)", this.getClass().getName(), dataTableRequestDto));

        try {
            Map<String, FilterInfoDto> filtersInfo = super.buildFiltersInfo(dataTableRequestDto);

            // create filter specifications
            Specification userSpec = SpecFactory.<AdmUsuarioEntity>getLikeSpecification(USUARIO, getValueAsString(filtersInfo, USUARIO));
            Specification firstNameSpec = SpecFactory.<AdmUsuarioEntity>getLikeSpecification(NOMBRES, getValueAsString(filtersInfo, NOMBRES));
            Specification lastNameSpec = SpecFactory.<AdmUsuarioEntity>getLikeSpecification(APELLIDOS, getValueAsString(filtersInfo, APELLIDOS));
            Specification emailSpec = SpecFactory.<AdmUsuarioEntity>getLikeSpecification(CORREO_ELECTRONICO, getValueAsString(filtersInfo, CORREO_ELECTRONICO));
            Specification positionSpec = SpecFactory.<AdmUsuarioEntity>getLikeSpecification(PUESTO, getValueAsString(filtersInfo, PUESTO));
            Specification displayNameSpec = SpecFactory.<AdmUsuarioEntity>getLikeSpecification(NOMBRE_MOSTRADO, getValueAsString(filtersInfo, NOMBRE_MOSTRADO));

            // assemble filter specifications
            return Specification
                    .where(getDefaultSpecification())
                    .and(userSpec)
                    .and(firstNameSpec)
                    .and(lastNameSpec)
                    .and(emailSpec)
                    .and(positionSpec)
                    .and(displayNameSpec);
        } catch (Exception ex) {
            LOGGER.error(ex.getMessage());

            throw new InvalidFilterException();
        }
    }

    @Override
    public PaginatedDataDto<AdmUsuarioEntity> findByPage(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        LOGGER.debug(String.format("@%s::findByPage(%s)", this.getClass().getName(), dataTableRequestDto));

        return super._findByPage(dataTableRequestDto, APELLIDOS);
    }

    @Override
    public AdmUsuarioEntity create(AdmUsuarioEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceCreateException {
        LOGGER.debug(String.format("@%s::create(%s, %d)", this.getClass().getName(), entity, requesterId));

        entity.setUsuario(entity.getCorreoElectronico().toLowerCase().split("@")[0]);
        entity.setCorreoElectronico(entity.getCorreoElectronico().toLowerCase());
        entity.setNombreMostrado(String.format("%s %s", entity.getNombres(), entity.getApellidos()));
        entity.setPuesto("-");
        entity.setActivo(true);

        return super._create(entity, requesterId);
    }

    @Override
    public AdmUsuarioEntity update(AdmUsuarioEntity entity, Long requesterId) throws RequesterNotFoundException, ResourceUpdateException {
        LOGGER.debug(String.format("@%s::update(%s, %d)", this.getClass().getName(), entity, requesterId));

        return super._update(entity, requesterId);
    }

    @Override
    public boolean deleteById(Long entityId) throws ResourceDeleteException {
        return false;
    }

    @Override
    public AdmUsuarioEntity deleteById(Long entityId, Long requesterId) throws RequesterNotFoundException, ResourceNotFoundException, ResourceUpdateException {
        LOGGER.debug(String.format("@%s::deleteById(%d, %d)", this.getClass().getName(), entityId, requesterId));

        AdmUsuarioEntity entity = this.findById(entityId);

        entity.setActivo(false);

        try {
            return super._update(entity, requesterId);
        } catch (RequesterNotFoundException | ResourceUpdateException ex) {
            LOGGER.error(ex.getMessage());

            throw ex;
        }
    }

    @Override
    public ModelAndView exportXlsx(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException {
        return null;
    }


    @Override
    public ResourceDto exportCsv(DataTableRequestDto dataTableRequestDto) throws ResourcesNotFoundException, InvalidFilterException, IOException {
        return null;
    }
}
