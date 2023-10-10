package spofo.support.service;

import org.springframework.beans.factory.annotation.Autowired;
import spofo.portfolio.service.PortfolioService;
import spofo.support.annotation.CustomServiceTest;

@CustomServiceTest
public abstract class ServiceTestSupport {

    @Autowired
    protected PortfolioService portfolioService;
	/*

	@Autowired
	protected RoleService roleService;

	@Autowired
	protected RoleQueryService roleQueryService;

	@Autowired
	protected RoleRepository roleRepository;

	@Autowired
	protected RoleHierarchyService roleHierarchyService;

	@Autowired
	protected RoleHierarchyQueryService roleHierarchyQueryService;

	@Autowired
	protected RoleHierarchyRepository roleHierarchyRepository;

	@Autowired
	protected PasswordService passwordService;

	@Autowired
	protected PasswordQueryService passwordQueryService;

	@Autowired
	protected PasswordEncoder passwordEncoder;

	@Autowired
	protected ResourceService resourceService;

	@Autowired
	protected ResourceQueryService resourceQueryService;

	@Autowired
	protected ResourceRepository resourcesRepository;
*/
}
