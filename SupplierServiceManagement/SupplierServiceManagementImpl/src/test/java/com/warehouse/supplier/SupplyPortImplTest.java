package com.warehouse.supplier;

import com.warehouse.commonassets.enumeration.PackageType;
import com.warehouse.commonassets.helper.Result;
import com.warehouse.commonassets.identificator.DepartmentCode;
import com.warehouse.commonassets.identificator.DepartmentId;
import com.warehouse.commonassets.identificator.DeviceId;
import com.warehouse.commonassets.identificator.SupplierCode;
import com.warehouse.commonassets.identificator.SupplierId;
import com.warehouse.commonassets.identificator.UserId;
import com.warehouse.supplier.domain.exception.SupplierAlreadyExistsException;
import com.warehouse.supplier.domain.enumeration.UserStatus;
import com.warehouse.supplier.domain.model.Supplier;
import com.warehouse.supplier.domain.port.primary.SupplyPortImpl;
import com.warehouse.supplier.domain.port.secondary.DepartmentServicePort;
import com.warehouse.supplier.domain.port.secondary.DeviceServicePort;
import com.warehouse.supplier.domain.port.secondary.SupplierRepository;
import com.warehouse.supplier.domain.port.secondary.UserServicePort;
import com.warehouse.supplier.domain.registry.DomainContext;
import com.warehouse.supplier.domain.service.*;
import com.warehouse.supplier.domain.vo.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEventPublisher;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class SupplyPortImplTest {

    private static final DepartmentCode DEPARTMENT_CODE = new DepartmentCode("GD1");

    private static final DepartmentId DEPARTMENT_ID = new DepartmentId(10L);

    @Mock
    private SupplierRepository supplierRepository;

    @Mock
    private DeviceServicePort deviceServicePort;

    @Mock
    private DepartmentServicePort departmentServicePort;

    @Mock
    private UserServicePort userServicePort;

    private SupplyPortImpl supplyPort;

    @BeforeEach
    void setup() {
        final SupplierService service = new SupplierServiceImpl(supplierRepository);
        final SupplierValidatorService validatorService = new SupplierValidatorServiceImpl(supplierRepository, deviceServicePort, departmentServicePort);
        final SupplierCodeGeneratorService generatorService = new SupplierCodeGeneratorServiceImpl();
        final DriverLicenseService driverLicenseService = new DriverLicenseServiceImpl();
        final ApplicationContext applicationContext = mock(ApplicationContext.class);
        final ApplicationEventPublisher eventPublisher = mock(ApplicationEventPublisher.class);
        final DomainContext domainContext = new DomainContext();
        domainContext.setApplicationEventPublisher(eventPublisher);
        domainContext.setApplicationContext(applicationContext);
        supplyPort = new SupplyPortImpl(service, generatorService, validatorService, driverLicenseService,
                deviceServicePort, departmentServicePort, userServicePort);
    }

    @Test
    void shouldCreateUserAccountForNewSupplier() {
        final SupplierCode supplierCode = new SupplierCode("123456");
        final SupplierCreateCommand request = new SupplierCreateCommand(supplierCode, "Adam", "Nowak", "+48111222333",
                "adam@example.com", DEPARTMENT_CODE);
        stubDepartment();
        when(userServicePort.createUserForSupplier(argThat(snapshot ->
                "adam@example.com".equals(snapshot.email()) && supplierCode.equals(snapshot.supplierCode()))))
                .thenReturn(new UserId(101L));

        supplyPort.create(request);

        verify(supplierRepository).create(argThat(supplier -> new UserId(101L).equals(supplier.createdUserId())
                && supplier.getUserStatus() == UserStatus.USER_CREATED));
    }

    @Test
    @DisplayName("Create supplier when supplier code is correct")
    void shouldCreateSupplier() {
        final SupplierCode supplierCode = new SupplierCode("123456");
        final SupplierCreateCommand request = createCommand(supplierCode);
        stubDepartment();
        final SupplierCreateResponse response = supplyPort.create(request);
        assertEquals(supplierCode, response.supplierCode());
    }

    @Test
    @DisplayName("Create supplier with department code")
    void shouldCreateSupplierWithDepartmentCode() {
        final SupplierCode supplierCode = new SupplierCode("123456");
        final DepartmentCode departmentCode = new DepartmentCode("GD1");
        final SupplierCreateCommand request = new SupplierCreateCommand(supplierCode, "test", "test", "test", null, departmentCode);
        when(departmentServicePort.validateDepartmentCode(departmentCode)).thenReturn(Result.success());
        when(departmentServicePort.getDepartmentId(departmentCode)).thenReturn(new DepartmentId(10L));

        final SupplierCreateResponse response = supplyPort.create(request);

        assertEquals(supplierCode, response.supplierCode());
        verify(supplierRepository).create(argThat(supplier -> new DepartmentId(10L).equals(supplier.getDepartmentId())));
    }

    @Test
    @DisplayName("Create supplier when supplier code is empty")
    void shouldCreateSupplierAutogeneratedCode() {
        final SupplierCreateCommand request = createCommand(null);
        stubDepartment();
        final SupplierCreateResponse response = supplyPort.create(request);
        assertNotNull(response.supplierCode());
    }

    @Test
    void shouldNotGenerateSupplierCodeWhenAlreadyOneExists() {
        final SupplierCode supplierCode = new SupplierCode("123456");
        final SupplierCreateCommand request = new SupplierCreateCommand(supplierCode, "test", "test", "test");
        when(supplierRepository.findByCode(supplierCode)).thenReturn(mock(Supplier.class));
        final Executable executable = () -> supplyPort.create(request);
        final SupplierAlreadyExistsException exception = assertThrows(SupplierAlreadyExistsException.class, executable);
        assertEquals(String.format("Supplier with code %s already exists", supplierCode), exception.getMessage());
    }

    @Test
    void shouldGetOneById() {
        final Supplier supplier = mock(Supplier.class);
        final SupplierId supplierId = new SupplierId(1L);
        when(supplierRepository.findById(supplierId)).thenReturn(supplier);
        final Supplier supplierDb = this.supplyPort.getOneById(supplierId);
        assertEquals(supplier, supplierDb);
    }

    @Test
    void shouldGetOneByCode() {
        final Supplier supplier = mock(Supplier.class);
        final SupplierCode supplierCode = new SupplierCode("123");
        when(supplierRepository.findByCode(supplierCode)).thenReturn(supplier);
        final Supplier supplierDb = this.supplyPort.getOneByCode(supplierCode);
        assertEquals(supplier, supplierDb);
    }

    @Test
    void shouldAddPackageType() {
        final ChangeSupportedPackageTypeCommand request = new ChangeSupportedPackageTypeCommand(new SupplierCode("123"), Set.of(PackageType.LARGE));
        when(supplierRepository.findByCode(new SupplierCode("123"))).thenReturn(mock(Supplier.class));
        this.supplyPort.addPackageType(request);
        verify(supplierRepository).update(any());
    }

    @Test
    void shouldAddDevice() {
        final DeviceId deviceId = new DeviceId("");
        final ChangeSupplierDeviceCommand request = new ChangeSupplierDeviceCommand(new SupplierCode("123"), deviceId);
        when(supplierRepository.findByCode(new SupplierCode("123"))).thenReturn(mock(Supplier.class));
        this.supplyPort.addDevice(request);
        verify(supplierRepository).update(any());
    }

    private static SupplierCreateCommand createCommand(final SupplierCode supplierCode) {
        return new SupplierCreateCommand(supplierCode, "test", "test", "test", null, DEPARTMENT_CODE);
    }

    private void stubDepartment() {
        when(departmentServicePort.validateDepartmentCode(DEPARTMENT_CODE)).thenReturn(Result.success());
        when(departmentServicePort.getDepartmentId(DEPARTMENT_CODE)).thenReturn(DEPARTMENT_ID);
    }
}
