package spofo.support.service;

import org.springframework.beans.factory.annotation.Autowired;
import spofo.portfolio.controller.port.PortfolioService;
import spofo.portfolio.service.port.PortfolioRepository;
import spofo.stock.service.StockServerService;
import spofo.stockhave.controller.port.StockHaveService;
import spofo.support.annotation.CustomServiceTest;
import spofo.tradelog.controller.port.TradeLogService;

@CustomServiceTest
public abstract class ServiceTestSupport {

    @Autowired
    protected PortfolioService portfolioService;

    @Autowired
    protected PortfolioRepository portfolioRepository;

    @Autowired
    protected StockServerService stockServerService;

    @Autowired
    protected StockHaveService stockHaveService;

    @Autowired
    protected TradeLogService tradeLogService;
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
