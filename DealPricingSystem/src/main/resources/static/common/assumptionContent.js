	priceToolConstants.factory('assumptionContent', function() {
		var assumptions = {
				generic : {
							//1
							'tr1': {'td1': 'Deal Information:',
									'td2': 'Start Date',
									'td3': 'Assumed start of the deal. Note that Q does not look to profile Transition in detail and therefore start date is the start of service. Transition price will be added (if requested) but added to an overall Year 1 price.Though the option is to set daily dates, remember that Q will work in whole-months only.'},
					   
							'tr2': {'td2': 'Deal Term',
									'td3': 'Assumed length of service in months. This is a whole number between 1 and 120 months.'},
					   
							'tr3': {'td2': 'Deal Phase',
								   	'td3': 'Phase in a procurement of the deal. Note this has no bearing on any price assessment but is used to record the phase should the price assessment result in becoming a Deal Submission into the Q database.'},
								   
						   	'tr4': {'td2': 'Country & Currency',
								   	'td3': 'The country selected will drive two potential outcomes in Q:<br>'+
											'Firstly, the country selected will determine the benchmark data returned as data is stored against varying countries.<br>'+
											'Secondly, past deal information will be converted to the chosen country to reflect any relevant country cost factors (such as labour differences) and also currency exchanges. Any currency translation will be done using the exchange rate at the point the past deal was submitted into the Q database, whereas a country-factor adjustment will be based upon more recent (annually updated) data.'},
							//2
						   'tr5': {'td1': 'Solution defaults:',
								   'td2': 'Offshore Allowed? Yes / No',
								   'td3': 'If selected then any subsequent Tower will have a default setting of Offshore Allowed although this can be changed by the user on a Tower by Tower basis. The offshore location used in the benchmark data primarily is India.'},
						   
						   'tr6': {'td2': 'Hardware Included? Yes / No',
								   'td3': 'If selected then any subsequent Tower will have a default setting of Hardware Included although this can be changed by the user on a Tower by Tower basis.'},
						   
						   'tr7': {'td2': 'Level of Service? High / Medium / Low',
								   'td3': 'If selected then any subsequent Tower will have a default setting of whichever level is chosen although this can be changed by the user on a Tower by Tower basis.'},
	
						   'tr8': {'td2': 'Service Governance',
								   'td3': 'This field allows the user to enter a % uplift that will be applied to the sum of the Tower prices. For example, if 10% is entered then an additional 10% per annum of the annual Tower charges will be added to the output section of the tool.<br>'+
										'Service Governance is to cover the management functions that sit outside of a specific Tower such as a CTO, Delivery Executive, HR etc.<br>'+
										'The tooltip provides the user with the lower quartile, higher quartile and database average information (version 2)'},
										   
						   'tr9': {'td2': 'Transition Fees',
								   'td3': 'This field allows the user to enter a % uplift that will be applied to the sum of the Year 1 Tower prices as a one off fee for the set-up of the services. Note this excludes any transformational activities such as migration to the cloud or device roll-outs.<br>'+
										'The tooltip provides the user with the lower quartile, higher quartile and database average information (version 2).'},
	
						   'tr10': {'td2': 'Migration Cost Applicable ',
								   	'td3': 'This field provides the user with an estimate price for transformation / migration projects, like server migration.'},
							
							//3
						   'tr11': {'td1': 'Assumptions:',
								   	'td2': 'Currency',
								   	'td3': 'Pricing may be based on past deals or benchmark information sourced from a country different to the country selected'},
			},
			
			hosting : {
					//1
					'tr1': {'td1': 'General Definitions:',
						   'td2': 'Operating System',
						   'td3': 'An operating system is a collection of software that manages computer hardware resources and provides common services for computer programs. The most important OS-Types for midrange servers are: <br>'
								+'Open Source: Images using Open Source operating systems, predominately Linux.<br>'
								+'UNIX: Images using the UNIX operating system <br>'
								+'Windows: Images using Windows operating system' },
				   
				   'tr2': {'td2': 'Server Images',
						   'td3': 'The count of OS instances in the environment supported within scope of the contract.'},
				   
				   'tr3': {'td2': 'Linux',
						   'td3': 'Linux is a family of Open Source Operating System.'},
						   
				   'tr4': {'td2': 'UNIX',
						   'td3': 'Unix is a family of operation systems that derive from the original AT&T Unix. Relevant for midrange servers are the variants: Oracle Solaris - for e.g. SPARC and x86-based systems IBM AIX - for e.g. IBM RS/6000 or PowerPC based systems HP UX - for e.g. HP 9000 systems'},
								
				   'tr5': {'td2': 'Windows',
					   	   'td3': 'Windows is an Operating System (OS) from Microsoft. Windows Standard is an edition which providers rent normally to clients.'},
					   		
			   	   'tr6': {'td2': 'Physical Server',
						   'td3': 'Servers having only one image per server. Physical server images are classified according to their size: Small, Medium, Large.'},
								  
				   'tr7': {'td2': 'Virtual Server',
						   'td3': 'Servers having images operating in an environment with multiple OS instances running on single server. To properly configure virtual images, users using the tool should not count the host system (or controller). The host system support pricing is covered over multiple virtual images. The average ratio of virtual images on a host system is approximately 16 images to one host system. Out of support does not affect unit pricing. Virtualization software is included in the price output (i.e., Hypervisor, VMware).'},
									   
				   'tr8': {'td2': 'Server Size',
						   'td3': 'Large - Servers in the range of 16-32 cores (Physical)/vCPU (Virtual) and 16-128 GB RAM Medium - Servers in the range of 4-8 cores (Physical)/vCPU (Virtual) and 8-32 GB RAM Small - Servers in the range of 1-2 cores (Physical)/vCPU (Virtual) and 2-8 GB RAM<'},
					
				   'tr26': {'td2': 'Database Instance',
						   'td3': 'The term “Database Instance” is used to describe a complete database environment, including the database software, table structure, stored procedures and other functionality. In difference to this general description the Database Model in ProBenchmark does not include software licensing cost (for the DBMS). Each instance manages several system databases and one or more user databases. Each server can run multiple instances of the Database Engine. Applications connect to the instance in order to perform work in a database managed by the instance.'},
						   
				   'tr27': {'td2': 'COTS',
						   'td3': 'COTS stands for Commercial off the Shelf. These conventional database management systems (DMS) such as Oracle and DB2. COTS instances supported typically have a medium level of complexity, and require support skillsets that are generally available in the market. Enter quantity of supported, medium to complex database COTS database installations/licenses (Oracle, Siebel, SAP). One license on a server = 1 database installation. The input into the model should include the count of the number of software installations supported. To count the database installations, ProBenchmark equates this to the software licensing and includes multiple database tables within the installation.'},
								   
				   'tr28': {'td2': 'SQL',
						   'td3': 'SQL stand for to Structured Query Language. Its a database language designed for managing data held in relational database management systems. Enter quantity of supported, low to medium complex databases instances (which requires a typical lower DBA skill to support e.g. for simple web pages with a limited number of database tables).'},
								
					//2
				   'tr9': {'td1': 'Q Settings & definitions:',
						   'td2': 'Offshore Allowed? Yes / No',
						   'td3': 'If selected the price will be based on deals that include offshore (note that the term offshore is inclusive of all typical offshore areas including Europe. Locations such as India will be included where generally relevant).'},
				   
				   'tr10': {'td2': 'Hardware Included? Yes / No',
						   'td3': 'If selected the price includes the provision of hardware and software (with software limited to the operating system layer only). Prices are based on a 4-year refresh for Probenchmark; Past Deals are subject to variations in refresh cycles.'},
				   
					   
				   'tr11': {'td2': 'Level of Service? High / Medium / Low',
							'td3': 'High: 99.90% - 99.99%, 7x24: The requirement to support application images with requirements of 99.90% to 99.99% availability on a 7x24 hours of operation basis.<br>'+
								   'Medium: 99.0% - 99.89%, 5x12-7x24: The requirement to support application images with requirements of 99.00% to 99.89% availability on a 5x12 to 7x24 hours of operations basis.<br>'+
								   'Low: Less than 99.0%, < 5x8-5x12: The requirement to support application images with requirements of less than 99.00% availability on a 5x8 to 5x12 hours of operations basis.'},

				   'tr12': {'td2': 'Tooling & Monitoring? Yes / No',
							'td3': 'If selected "Yes" then includes the following:<br>'+
									'Activities including software updates, monitoring and scheduling, bug fixes and patches, remote access support, hardware diagnostics, and full batch production support.<br>'+
								    'Monitoring covers the operational activities associated with observing the regular operations of the related hardware and applications. Activities include responding to alerts, performing stress tests, collecting performance information, passing incidents on to next level support.'},
					
				   'tr13': {'td2': 'Co-location? Fujitsu / Client',
						   	'td3': 'If Fujitsu is selected then the price includes space & power associated with the datacentre provision.<br>'+
								'	If client is selected the servers are assumed to be in a client datacentre and space & power is excluded from the price.'},
					
					//3
				   'tr14': {'td1': 'Included in the price (irrespective of settings):',
						   'td2': ' Management Tools',
						   'td3': 'Equipment, devices and software (HW and SW) used to monitor, prevent and/or take corrective action of the IT environment operations. This may include but is not limited to: image configuration viewers, usage monitoring of RAM, disks, and processors, capacity management, software repair and management, performance management. For Desktop Services, Software Distribution Tools are also included.'},
				   
				   'tr15': {'td2': 'Disaster Recovery',
						   'td3': 'Support of the customer requirements for operational functions necessary to prepare the environment for disaster recovery tests. Excludes third party DR contracts and equipment (unless included in deal configuration).<br>'+
								  'Included in the price is one DR test per year.'},
				   
					   
				   'tr16': {'td2': 'Server Support',
							'td3': 'Level 2 Problem Resolution is comprised of more senior technicians that solve more complex problem tickets that could not be resolved by the Service Desk.<br>'+
							 	   'Level 3 problem resolution activities associated with hosting support. The onsite job and remote management activities such as tape management, configuration management, hardware support, production control, and Level 3 problem resolution.'},

				   'tr17': {'td2': 'System Administration',
							'td3': 'Delivery activities including but not limited to: installation and configuration of common services, image monitoring, OEM reimbursed hardware and software maintenance (coordination and performance of covered warranty services as required), software and security upgrades, trouble-shooting of failing services, and ID administration.'},
					
				   'tr18': {'td2': 'PMO',
						   	'td3': 'Program Management Office (PMO) relates to the ITIL Design Process which is the activity that identifies requirements and defines a solution to meet those requirements.'},
								
				   'tr19': {'td2': 'Security Services',
					   		'td3': 'Basic security monitoring and protocols for intrusion detection (HIDS, NIDS), antivirus support and updating, and incident logging including regular status reporting. Manage SPAM and virus filtering. This basic level includes user ID implementation. Most configuration management, policies and procedures, and problem response are not within scope.'},			
								
		   			//4
				   'tr20': {'td1': 'Excluded from the price (irrespective of settings):',
						   'td2': 'Transition',
						   'td3': 'Transition is excluded from the individual Tower pricing and should be added as part of a Q price assessment from within the Generic Inputs screen'},
				   
				   'tr21': {'td2': 'Transformation',
							'td3': 'Any transformational activities such as server migrations to the cloud, upgrades and re-platforming'},

				   'tr22': {'td2': 'Procurement',
							'td3': 'Where Client is financially responsible for any underlying equipment, software, or activities related to the services (when ‘No’ is selected for Hardware included), performing the procurement order processing, tracking, and verification processes is excluded.'},
					
				   'tr23': {'td2': 'Security Procedures',
						    'td3': 'Update, maintain and distribute customer security procedures.'},
					
				   'tr24': {'td2': 'Problem Management',
						   	'td3': 'Covered within Service Management Transition and should be added as part of a Q price assessment from within the Generic Inputs screen. A problem is a cause of one or more incidents and the cause is not typically known at the time a problem is reported. The problem management process is responsible for further investigation. Problem Management is an activity within Operations and it is the process responsible for managing the lifecycle of all problems, proactively preventing incidents from occurring and minimizing the impact to business when they cannot be prevented. Activities include; performing proactive and reactive troubleshooting to effectively identify and resolve problems, employing procedures for proactive monitoring, logging, tracking, escalation, review and reporting (historical and predictive) for all problems.'},
								
					//5
				   'tr25': {'td1': 'Assumptions:',
						   'td2': 'Default Settings',
						   'td3': 'As part of a simple Price Assessment, a user may enter volumes at a high level. When this is done there are two options that can be chosen that create default lower level settings that Q uses to calculate the average server prices. These options include:-<br>'+
								'Traditional: [TBC]<br>'+
						   		'Hybrid: [TBC]'},
						   
		},
		
		storage : {
				//1
				'tr1': {'td1': 'General Definitions:',
					   'td2': 'Performance Storage',
					   'td3': 'Performance storage is primarily meant to include business critical and rapid accessible storage. Pricing is based upon a 15% Tier 0 and 85% Tier 1 split in Probenchmark.' },
			   
			   'tr2': {'td2': 'Standard Storage',
					   'td3': 'Standard storage is primarily meant to include regular performing and accessible storage. Used for the majority of volume available, like file-based storage. Pricing is based upon a 50% Tier 2 and 50% Tier 3 split in Probenchmark.'},
			   
			   'tr3': {'td2': 'Back-up',
					   'td3': 'Backup is the activity of copying files or databases so that they will be preserved in case of equipment failure or other catastrophe. Backup is a routine part of the operation.'},
					   
			   'tr4': {'td2': 'TB Storage Volume',
					   'td3': 'Volume is measured by the Usable monthly TB of Storage charged in the contract. <br>'+
							  'Usable refers to amount of storage available for the customer to use. This is also known as allocated storage. '},
							
			   'tr5': {'td2': 'TB Back-up Volume',
				   	   'td3': 'Volume backed-up in any month factoring in daily, weekly and monthly back-up cycles. Typically a small proportion of stored data is backed up daily, more weekly and more monthly with a total amount relative to TB stored being in the region of 50% to 150% [TBC]'},
				   		
							
				//2
			   'tr6': {'td1': 'Q Settings & definitions:',
					   'td2': 'Offshore Allowed? Yes / No',
					   'td3': 'If selected the price will be based on deals that include offshore (note that the term offshore is inclusive of all typical offshore areas including Europe. Locations such as India will be included where generally relevant).'},
			   
			   'tr7': {'td2': 'Hardware Included? Yes / No',
					   'td3': 'If selected the price includes the provision of hardware and software. Prices are based on a 3-year refresh for Probenchmark; Past Deals are subject to variations in refresh cycles.<br>'+
							'If selected it is also assumed that maintenance is included in the price.  Maintenance includes needed repair or upgrades on identified equipment.'},
			   
				   
			   'tr8': {'td2': 'Level of Service? High / Medium / Low',
						'td3': 'High: 99.90% - 99.99%, 7x24: The requirement to support storage with requirements of 99.90% to 99.99% availability on a 7x24 hours of operation basis. Premium hardware is used to achieve the high level of availability. In addition High Service level will include Architecture, Testing and Planning and also Capactiy, utilisation and performance monitoring.<br>'+
							'Medium: 99.0% - 99.89%, 5x12-7x24: The requirement to support storage with requirements of 99.00% to 99.89% availability on a 5x12 to 7x24 hours of operations basis. Standard hardware is used to achieve the medium level of availability. In addition Medium Service level will include Capactiy, utilisation and performance monitoring.<br>'+
							'Low: Less than 99.0%, < 5x8-5x12: The requirement to support storage with requirements of less than 99.00% availability on a 5x8 to 5x12 hours of operations basis. Standard hardware is used to achieve the lower level of availability.'},

				//3
			   'tr9': {'td1': 'Included in the price (irrespective of settings):',
					   'td2': ' Management Tools',
					   'td3': 'Equipment, devices and software (HW and SW) used to monitor, prevent and/or take corrective action of the IT environment operations. This may include but is not limited to: image configuration viewers, usage monitoring of RAM, disks, and processors, capacity management, software repair and management, performance management. For Desktop Services, Software Distribution Tools are also included.'},
			   
			   'tr10': {'td2': 'Storage & Back-up Support',
					   'td3': 'Level 2 Problem Resolution is comprised of more senior technicians that solve more complex problem tickets that could not be resolved by the Service Desk.<br>'+
							 'Level 3 problem resolution activities associated with storage support. The onsite job and remote management activities such as tape management, configuration management, hardware support, production control, and Level 3 problem resolution.'},
			   
			   'tr11': {'td2': 'PMO',
					   	'td3': 'Program Management Office (PMO) relates to the ITIL Design Process which is the activity that identifies requirements and defines a solution to meet those requirements.'},
							
			   'tr12': {'td2': 'Security Services',
				   		'td3': 'Basic security monitoring and protocols for intrusion detection (HIDS, NIDS), antivirus support and updating, and incident logging including regular status reporting. Manage SPAM and virus filtering. This basic level includes user ID implementation. Most configuration management, policies and procedures, and problem response are not within scope.'},			
							
	   			//4
		   		'tr13': {'td1': 'Excluded from the price (irrespective of settings):',
					   'td2': 'Transition',
					   'td3': 'Transition is excluded from the individual Tower pricing and should be added as part of a Q price assessment from within the Generic Inputs screen'},
			   
			   'tr14': {'td2': 'Transformation',
						'td3': 'Any transformational activities such as server migrations to the cloud, upgrades and re-platforming'},

			   'tr15': {'td2': 'Procurement',
						'td3': 'Where Client is financially responsible for any underlying equipment, software, or activities related to the services (when ‘No’ is selected for Hardware included), performing the procurement order processing, tracking, and verification processes is excluded.'},
				
			   'tr16': {'td2': 'Security Procedures',
					    'td3': 'Update, maintain and distribute customer security procedures.'},
				
			   'tr17': {'td2': 'Problem Management',
							   	'td3': 'Covered within Service Management Transition and should be added as part of a Q price assessment from within the Generic Inputs screen. A problem is a cause of one or more incidents and the cause is not typically known at the time a problem is reported. The problem management process is responsible for further investigation. Problem Management is an activity within Operations and it is the process responsible for managing the lifecycle of all problems, proactively preventing incidents from occurring and minimizing the impact to business when they cannot be prevented. Activities include; performing proactive and reactive troubleshooting to effectively identify and resolve problems, employing procedures for proactive monitoring, logging, tracking, escalation, review and reporting (historical and predictive) for all problems.'},
							
				//5
			   'tr18': {'td1': 'Assumptions:',
					   'td2': 'Default Settings',
					   'td3': 'As part of a simple Price Assessment, a user may enter volumes at a high level. When this is done there are two options that can be chosen that create default lower level settings that Q uses to calculate the average server prices. These options include:-<br>'+
							'For Storage:<br>'+
					   		'Normal - 20% performance storage, 80% standard performance storage<br>'+
				   			'FAST - 40% performance storage, 60% standard performance storage<br>'+
				
				   			'For Back-up - Frequency factor (converts storage volumes into back-up volumes):<br>'+
				   			'High - 1.2 times the total storage volume.<br>'+
				   			'Medium - 1.0 times the total storage volume.<br>'+
				   			'Low - 0.8 times the total storage volume.'},
					   
		},
		
		endUser : {
			//1
			'tr1': {'td1': 'General Definitions:',
				   'td2': 'Desktop',
				   'td3': ' A Desktop PC is a non-portable computer. Included components: PC, monitor, keyboard, mouse, HDD, optical disk drive, Windows OS (professional edition), Anti-Virus SW.' },
		   
		   'tr2': {'td2': 'Laptop',
				   'td3': 'A laptop is a portable computer with own keyboard and screen.<br>'+
					 		'Standard - It has traditionally been referred to as the Laptop and is designed to be small and lightweight enough to be transported by the user on a daily basis, commonly integrates most of the typical components of a desktop computer, including a display, a keyboard, and a pointing device and can have different form factors that differentiate them primarily based upon screen size and weight, primarily used in office locations with docking stations.<br>'+
					 		'High End - Difference to Standard: Ultra-Portable and/or with a faster configuration than Standard.'},
		   
		   'tr3': {'td2': 'Thin Client',
				   'td3': 'Device where the desktop operating system is hosted within a virtual machine running on a centralised server.'},
				   
		   'tr4': {'td2': 'Mobile Device',
				   'td3': 'Any tablet or similar mobile device (but not a mobile phone or laptop).'},
						
		   'tr5': {'td2': 'IMAC',
			   	   'td3': 'IMACs (Installations, Moves, Adds and Changes) covers all activities associated with the planning, scheduling, managing and/or performing of the operational change activity as requested and approved by the client. Change activity includes the addition, modification or removal of anything that could effect IT services. Project related changes are not included as IMAC activity.'},
			   		
			//2
		   'tr6': {'td1': 'Q Settings & definitions:',
				   'td2': 'Offshore Allowed? Yes / No',
				   'td3': 'If selected the price will be based on deals that include offshore (note that the term offshore is inclusive of all typical offshore areas including Europe. Locations such as India will be included where generally relevant).'},
		   
		   'tr7': {'td2': 'Hardware Included? Yes / No',
				   'td3': 'If selected the price includes the provision of hardware and software (with software limited to the operating system). Prices are based on a 5-year refresh for Probenchmark; Past Deals are subject to variations in refresh cycles.'},
		   
			   
		   'tr8': {'td2': 'Level of Service? High / Medium / Low',
					'td3': 'High: Severity 1 less than 2 hours; Severity 2 less than 4 hours.<br>'+
						   'Medium: Severity 1 between 4 and 6 hours; Severity 2 between 6 and 8 hours.<br>'+
						   'Low: Severity 1 within 8 hours or more; Severity 2 within 10 hours or more.<br>'+
						   'Resolution time refers to the total time required to resolve a reported problem, as measured from the point the problem is reported to the Vendor to the time it is completed and ticket is closed out. Severity 1 - Follows Typical severity one specifications, normally including a group of end users experiencing an outage simultaneously; 5x10 hours of operation. Severity 2 - Follows typical Severity 2 specifications, normally an outage for a single end user'},
			
		   'tr9': {'td2': 'Include Breakfix? Yes / No',
			   		'td3':'If selected ‘Yes’ then includes the following:<br>'+
			   			  'Maintenance (or Maintenance & Repair) including restoring failed hardware to an acceptable working state. The cost for any necessary OEM supported equipment would be included.'},

			//3
		   'tr10': {'td1': 'Included in the price (irrespective of settings):',
				   'td2': 'Central Software Distribution',
				   'td3': 'The support related to distribution of software updates to end user devices in the environment. Typically, includes distribution of client antivirus, standard S/W patches and standard S/W updates to the client devices, and excludes labour related to the packaging/build of standard desktop applications.'},
		   
		   'tr11': {'td2': 'Support Services',
				   'td3': 'Include:<br>'+
				   		'Level 2 Problem Resolution - comprised of more senior technicians that solve more complex problem tickets that could not be resolved at Level 1 - Service Desk. May include coordinating desk-side assistance and lab support for issues such as: printer jams, network issues, standard software or hardware issues. '},
		   
		   'tr12': {'td2': 'Software Maintenance & Repair',
			   		'td3': 'Identification, problem determination, resolution, image backup/restore, and soft IMAC`s for standard software applications included in the supported desktop image.'},
									 
 		   'tr13': {'td2': 'Supported Images',
	   		   		 'td3':'Support of up to 3 images. An image is a pre-configured, packaged installation disk or image that contains corporate standard software products. This typically includes the MS products and standard end user applications (i.e., time and expense reporting, email), but not custom software applications.'},
	   		   
   		   'tr14': {'td2': 'LAN User Management',
				   		'td3': 'The management of LAN user accounts, access rights and other administrative activities in regards to end users.'},
					 		
 		   'tr15': {'td2': 'PMO',
				   	'td3': 'Program Management Office (PMO) relates to the ITIL Design Process which is the activity that identifies requirements and defines a solution to meet those requirements.'},
						
		   'tr16': {'td2': 'Security Services',
			   		'td3': 'Basic security monitoring and protocols for intrusion detection (HIDS, NIDS), antivirus support and updating, and incident logging including regular status reporting. Manage SPAM and virus filtering. This basic level includes user ID implementation. Most configuration management, policies and procedures, and problem response are not within scope.'},			
   		  			
   			//4
	   		'tr17': {'td1': 'Excluded from the price (irrespective of settings):',
				   'td2': 'Transition',
				   'td3': 'Transition is excluded from the individual Tower pricing and should be added as part of a Q price assessment from within the Generic Inputs screen'},
		   
		   'tr18': {'td2': 'Transformation',
					'td3': 'Any transformational activities such as server migrations to the cloud, upgrades and re-platforming'},

		   'tr19': {'td2': 'Procurement',
					'td3': 'Support of purchasing process of hardware and software. The service activities involve providing advice on compatibility, configuration, and integration requirements. Where Fujitsu is financially responsible for any underlying equipment then price includes performing all tasks associated with its procurement orders, tracking, and verification activities.<br>'+
						'Where Client is financially responsible for hardware (not included in price) then procurement is limited to advice, order placement and tracking. In most cases the equipment is located on client sites, shipping and receiving becomes the responsibility of the client.'},
			
		   'tr20': {'td2': 'Security Procedures',
				    'td3': 'Update, maintain and distribute customer security procedures.'},
			
		   'tr21': {'td2': 'Problem Management',
						   	'td3': 'Covered within Service Management Transition and should be added as part of a Q price assessment from within the Generic Inputs screen. A problem is a cause of one or more incidents and the cause is not typically known at the time a problem is reported. The problem management process is responsible for further investigation. Problem Management is an activity within Operations and it is the process responsible for managing the lifecycle of all problems, proactively preventing incidents from occurring and minimizing the impact to business when they cannot be prevented. Activities include; performing proactive and reactive troubleshooting to effectively identify and resolve problems, employing procedures for proactive monitoring, logging, tracking, escalation, review and reporting (historical and predictive) for all problems.'},
			
	       'tr22': {'td2': 'Premise Network Administration',
		          	'td3': 'Refers to the administration and maintenance of the LAN network infrastructure (e.g., switches, hubs). '},
			
          	//5
		   'tr23': {'td1': 'Assumptions:',
				   'td2': 'Default Settings',
				   'td3': 'As part of a simple Price Assessment, a user may enter volumes at a high level. When this is done there are two options that can be chosen that create default lower level settings that Q uses to calculate the average server prices. These options include:-<br>'+
					   		'[include IMAC assumption]'},
				   
		   'tr24': {'td2': 'Location',
			        'td3': 'Location refers to the facility where the HW resides. It is assumed that the devices are in an office location within a metropolitan area or at a large office location with the scale to support full-time onsite staff providing support within the area.<br>'+
			        		'Smaller sites would typically require a price uplift but this is not calculated in Q.'},
			},
			
			network : {
				//1
				'tr1': {'td1': 'Definitions:',
					   'td2': 'LAN Switch',
					   'td3': 'LAN Switches are devices which connect the computer network together, they filter and forward packets between LAN segments. Large - 48+ ports (10/100/1000 MB), Layer 4-7 protocol, Multiple Gigabit interface, Metropolitan rated and typically reflects environments that support thousands of users. Example - Cisco 5xxx series, Aruba 3400, 3600, 5000, and 6000 series, or equivalent. Medium - 24 – 48 ports (10/100/1000) MB, Layer 2-4 protocol, Multiple Gigabit interface, Campus rated and reflects environments that support hundreds of users. Example - Cisco 3xxx & 4xxx series, Aruba 2400 and 3200 series, or equivalent. Small - 12-24 ports, 10/100 MB ports, Layer 2 protocol, Gigabit support. Example - Cisco 2xxx series, Aruba 200, 600, and 800 series or equivalent Very Small - Up to 12 ports, Typically supporting small office or terminal ' },
			   
			   'tr2': {'td2': 'Load Balancer',
					   'td3':  'A server or devices used to detect and re-route WAN traffic across devices or network links to achieve optimal efficiency of the network. Services and Functions include: - Maximize throughput, - Minimize response time, - Avoid overload, - Manage different quality of service targets by application and type of packet.'},
			   
			   'tr3': {'td2': 'WAN Accelerator',
					   'td3': ' WAN accelerator appliance can be a physical hardware component, software program or an appliance running in a virtualized environment. The appliance optimizes the WAN bandwidth through compression and data deduplication techniques reducing the time it takes for information to traverse the WAN. Services and Functions include: - Leverage compression and data duplication techniques to reduce the volume of data transmitted over the WAN.'},
					   
			   'tr4': {'td2': 'WAN Router',
					   'td3': ' WAN Routers are intelligent devices that direct data packet traffic between networks. Large - 12-24 ports, 15 to 30 Mbps speed, modular or rackable, Includes integrated security services Example - Cisco 7xxx and ASR 1xxx; Juniper M7i, MX10, and SRX650). Medium - 8-12 ports, modular or rackable Example - Cisco 3xxx - 4xxx series; Adtran Netvanta Series 4xxx and 5xxx; Juniper J63xx, and SRX 5xx and 6xx; Nortel Series 3xxx. Small - 4-8 Ports, stand-alone chassis, Speed up to 1Gbps, Can include Wi-Fi Example - Cisco 2xxx series; Juniper 2xx, J23xx, and J43xx, Adtran Netvanta Special 3201, Netvanta Series 2xxx, 3xxx; Adtran Total Access Series 9xx; Nortel Series 1xxx; Digi WR-44; MBR6xx. Very Small - Up to 4 ports. Example: Cisco 8xx - 18xx series, Juniper SRX 1xx, Netvanta Series 1xxx, Cradlepoint IBR1xxx'},
							
			   'tr5': {'td2': 'WAP',
				   	   'td3': ' Wireless Access Points (WAPs) are devices used to connect devices on a wireless network to a wired network using Wi-Fi, Bluetooth or other related standard. They are the relay between the wireless devices (such as computers or printers) and wired devices on the network. The AP usually connects to a router (through the wired network) but can also be a component of the router. Services and Functions include providing wireless connectivity to the LAN.'},
				   		
		   	   'tr6': {'td2': 'Wireless Access Controller',
		   		   		'td3': 'A WLAN Controller is a centralized device, commonly found in the data center, which connects multiple wireless access points (WAPs). Provides centralized wireless network visibility and control offering administrators access to multiple WAPs for configuration, troubleshooting and management. Services and Functions include: - Automatically handles the configuration of wireless access-points, - Features Interference detection and avoidance, - Load Balancing, - Coverage hole detection (Part of the RF management is the ability to handle power levels. Power can be increased to cover holes or reduced to protect against cell overlapping), - Rogue detection, - Secure guest access, - Various forms of authentication such as: 802.1X (Protected Extensible Authentication Protocol (PEAP), LEAP, EAP-TLS, Wi-Fi Protected Access (WPA), 802.11i (WPA2), and Layer 2 Tunneling Protocol (L2TP).'},
				   	   
				//2
			   'tr7': {'td1': 'Q Settings & definitions:',
					   'td2': 'Offshore Allowed? Yes / No',
					   'td3': 'If selected the price will be based on deals that include offshore (note that the term offshore is inclusive of all typical offshore areas including Europe. Locations such as India will be included where generally relevant).'},
			   
			   'tr8': {'td2': 'Hardware Included? Yes / No',
					   'td3': 'If selected the price includes the provision of hardware and software (with software limited to the operating system). Prices are based on a 5-year refresh for Probenchmark; Past Deals are subject to variations in refresh cycles.'},
			   
				   
			   'tr9': {'td2': 'Level of Service? High / Medium / Low',
						'td3': 'The Level of Service impacts several elements, impacting the quality and extensiveness of the service.<br>'+
							'High:<br>'+
							'WAN/LAN Availability: Network Availability: > 99.97%, Resolution Time Severity 1: 1 hour (or less), Resolution Time Severity 2: 2 hours (or less)<br>'+
							'Full Services included: Asset Management, Procurement, Network Cable/Wiring Management, Hard-Software maintenance coordination<br>'+
							'Medium:<br>'+
							'WAN/LAN Availability: Network Availability: Between 99.90% and 99.97%, Resolution Times Severity 1: 2 hours, Resolution Time Severity 2: 6 hours<br>'+
							'Partial Services included: Asset Management, Procurement, Network Cable/Wiring Management, Hard-Software maintenance coordination<br>'+
							'Low:<br>'+
							'WAN/LAN Availability: Network Availability: <99.90%, Resolution Times Severity 1: 4 hours or more, Resolution Time Severity 2: Next Business Day<br>'+
							'Limited Services included: Asset Management, Procurement, Network Cable/Wiring Management, Hard-Software maintenance coordination'},
				
			   'tr10': {'td2': 'LAN Severity 1 Resolution',
				   		'td3': 'High - 1 Hour or Less. High availability environment assumes redundancies are included as part of the architecture. May include switch redundancy and no tolerance for any disruption. Medium - 2 Hours. Typical enterprise environment requiring accelerated resolution times. High transaction environments. Low - 4 hours or more. Batch or reduced real-time transaction based environment. Work flow less real-time dependent. '},
		   		
	   		   'tr11': {'td2': 'LAN Severity 2 Resolution',
				   		'td3': 'High - 2 Hours or Less. Doesn`t represent complete loss of connectivity, may be segment affecting only. Medium - 4 Hours. Doesn`t represent complete loss of connectivity, may be segment affecting only. Low - 6 hours or more. Doesn`t represent complete loss of connectivity, may be segment affecting only.'},
					   		
	   		   'tr12': {'td2': 'WAN Severity 1 Resolution',
				   		'td3': 'High - One hour or less response time. Assumes a high availability environment, typically with redundancies included as part of architecture. May include Router redundancy. No tolerance for any disruption. Medium - Two hour response time. Typical Enterprise environment requiring accelerated resolution times. High transaction environments. Low - 4 hours or more. Batch or reduced real-time transaction based environment. Work flow less real-time dependent.'},
						   		
	   		   'tr13': {'td2': 'WAN Severity 2 Resolution',
				   		'td3': 'High - 2 Hours or Less. Doesn`t represent complete loss of connectivity, may be segment affecting only. Medium - 4 Hours. Doesn`t represent complete loss of connectivity, may be segment affecting only. Low - 6 hours or more. Doesn`t represent complete loss of connectivity, may be segment affecting only. '},
				   		
	   		   'tr14': {'td2': 'Asset Management',
	   			   		'td3':'The management, oversight and on-going upkeep of all hardware and software (active and inactive) on an asset management system and/or an asset inventory and management system, to include a complete profile of the equipment and software in use by authorized users. The scope includes updates based on IMAC activity and periodic asset reports to client, as directed. The model output does not include the initial asset inventory, which is normally included as part of the transition and transformation charge.'},
	   			   	
	   		   'tr15': {'td2': 'Procurement',
   			   			'td3':'Support of the purchasing process of hardware and software. The service activities involve providing advice on compatibility, configuration, and integration requirements, order placement and tracking. In most cases the equipment is located on client sites, shipping and receiving becomes the responsibility of the client. Yes - is selected when the provider is financially responsible for any underlying equipment, software, or activities related to the services, performing all tasks associated with its procurement orders, tracking, and verification activities. No - is selected when the client is financially responsible for any underlying equipment, software, or activities related to the services, performing the procurement order processing, tracking, and verification processes. '},
		   	   
   				'tr16': {'td2': 'Network Cable/Wiring Management',
   			   			'td3': 'Refers to the planning, procuring, installing, operating, administering, maintaining, and managing the cabling and intra-floor and inter-floor wiring at Client sites, including the physical cable plant, cable plant switching devices, intelligent/non-intelligent wiring hubs, and the various monitoring devices. Providing third-party Vendor management of the Client Vendors who are managing cable installations, repairs, and removal using a software-based cable plant management system where applicable, interacting with Client facilities, landlord management and other authorized users to ensure cabling and wiring requirements are properly communicated and managed.'},
	   			   		
		   		'tr17': {'td2': 'Hardware/Software Maintenance Coordination',
	   			   		'td3': 'The coordination and management of all third parties that provide maintenance related support for equipment and software and performing these responsibilities regardless of whether Vendor or Client has financial responsibility for the underlying asset and maintenance expenses. Providing such maintenance as necessary to keep the assets in good operating condition and in accordance with manufacturer’s specifications or other agreements as applicable, so that such assets will qualify for the manufacturer’s standard maintenance plan upon sale or return to a lessor.'},

				//3
			   'tr18': {'td1': 'Included in the price (irrespective of settings):',
					   'td2': 'Program Management Office',
					   'td3': 'Program Management Office (PMO) relates to the ITIL Design Process which is the activity that identifies requirements and defines a solution to meet those requirements. PMO activities are typically captured within other defined processes for example management activities within Service Level Management but not service level definition, maintenance or enforcement. PMO activities are included in the following sub processes: - Architecture Management, - Availability Management, - Capacity Management, - Compliance Management, - Information Security Management, - Risk Management, - Service Catalog Management, - Service Continuity Management, - Service Level Management and - Supplier Management. PMO is a required field in most towers but may be optionally included or excluded in some towers.'},
			   
			   'tr19': {'td2': 'Network Engineering/Planning & Design',
					   'td3': 'Refers to the coordination of the engineering, planning and design effort associated with client’s network. Provision of a high level expertise related to hardware, software and network solutions.'},
			   
			   'tr20': {'td2': 'Testing & Integration',
				   		'td3': 'Refers to the coordination and management of testing and integration of new hardware and software related to the implementation of any changes, upgrades, or enhancements within the IT towers. Develop for Customer’s review and approval testing plans, data and expected results for integration testing, volume stress testing as appropriate, and quality assurance testing of the equipment in the data network and voice network equipment environments and burn in testing with respect to equipment in the data network, and voice network environments. Such plans and data shall be sufficiently robust to identify any material errors or problems; develop acceptance criteria and procedures for acceptance of new equipment and upgrades to and replacements of equipment installed in customer’s environment and the data center; perform testing and recommending acceptance in accordance with the plans, criteria and procedures approved; and resolve errors and other problems identified during testing of equipment.'},
										 
	 		   'tr21': {'td2': 'IMAC',
		   		   		 'td3':'IMACs (Installations, Moves, Adds and Changes) covers all activities associated with the planning, scheduling, managing and/or performing of the operational change activity as requested and approved by the client. Change activity includes the addition, modification or removal of anything that could effect IT services. The scope of the change activity is determined within each tower based on the equipment identified in the scenario. Project related changes are not included as IMAC activity but rollouts for new devices after a device life cycle are also IMACs. Some towers have locked IMAC as included in the price output of the scenario.'},
		   		   
	   		   'tr22': {'td2': 'DNS/DHCP Support',
				   		'td3': 'DNS (Domain Name System) servers provide domain name resolution for network resources and DHCP (Dynamic Host Configuration Protocol) automatically assigns dynamic IP addresses to devices on a network, both services managing IP connectivity. Support services include: - Providing name resolution for all client-owned/registered domains along with forwarding name resolution services for Intranet and DNS-based services resolving Internet names. - Provide primary/authoritative DNS, including forward and reverse zones, for top-level domain names. - Centrally manage and automate the assignment of Internet Protocol (IP) addresses in an organization`s network. - Setup the DHCP service and designate IP address ranges. Setup includes: - Creating IP address and mask of the workstation. - Gateway address for the workstation to reach the Internet. - Setup and maintain DNS server so the workstation can resolve Web addresses to IP address on the Internet. - Additional options can include maintaining other servers and providing support services for: Log Servers, Time-Servers, Cookie Servers, Line Printers.'},
						 		
	 		   'tr23': {'td2': 'Security Policy Management',
					   	'td3': 'Implementing and maintaining security procedures, tools and system requirements to protect the integrity, confidentiality, and availability of the Network and data on the Network. Comply with client`s Network security policies and Network Architecture and Standards. Perform regular assessments of risk exposure. Monitoring usage patterns and investigating and reporting significant discrepancies in those patterns.'},
							
			   'tr24': {'td2': 'VPN Authentication',
				   		'td3': 'Management of the authentication process related to remote access to corporate networks via VPN (Virtual Private Network) with either a hardware solution (smart card or USB token), soft tokens, or a combination of both. Setup and maintenance of single & multi-domain policy servers to manage users, rules, and logs for each VPN, manage policy backup and restore, provide real-time and historical monitoring, logging, notification, and reporting.'},			
				
		   		'tr25': {'td2': 'Capacity, Change, Configuration Management and Planning',
					   'td3': 'Capacity Management includes assessing, managing, monitoring and potentially changing network voice and data capacities to meet agreed upon service levels. Change Management - see separate description Configuration Management provides for the common goal of a logical model of the IT infrastructure by identifying, controlling, maintaining and verifying the versions of all configuration items in existence. Additional functions and activities performed include: - Monitoring Network capacity utilization as it relates to established capacity thresholds. - Identifying future loads that could impact performance on the Network as requested by Client with a minimum forward view of 12 months, based on forecasts driven by the work authorization system, demand surveys, and analytical forecasting. - Proposing to Client for their approval, changes to improve performance in anticipation of future loads, performance improvement expectations, sizing inter-location and intra-location network equipment, software, and transport systems. - Developing and implementing an overall Change Management process to manage changes to the Services, subject to approval from Client. - Coordinating Change Management activities across all functions, towers, client sites, regions, subcontractors, client third party contractors and other third parties. - Deploying tools to automate the process of scheduling, describing, tracking, and reporting on changes to the environment. - Ensuring that the Change Management process achieves a set of clearly defined objectives including, efficient implementation of change, clear accountability, minimization of risk, minimization of business disruption, and effective coordination and communication. '},
			   
			   'tr26': {'td2': 'Performance Analyses & Tuning Measures',
						'td3': 'The proactive and continual delivery activity of maximizing the IT environment performance including; the design of service configurations using data gathered from performance monitoring and forecasting activities; performing regular optimization analyses on a regular basis, prior to and following any major transitions or changes; and optimizing cost-effectiveness and cost-efficiency, without sacrificing the performance or ability to meet the Service Levels. '},

			   'tr27': {'td2': 'Problem Management',
						'td3': 'A problem is a cause of one or more incidents and the cause is not typically known at the time a problem is reported. The problem management process is responsible for further investigation. Problem Management is an activity within Operations and it is the process responsible for managing the lifecycle of all problems, proactively preventing incidents from occurring and minimizing the impact to business when they cannot be prevented. Activities include; performing proactive and reactive troubleshooting to effectively identify and resolve problems, employing procedures for proactive monitoring, logging, tracking, escalation, review and reporting (historical and predictive) for all problems.'},
				
			   'tr28': {'td2': 'Single point of contact',
					    'td3': 'Single point of contact refers to a central help desk function used to manage calls for trouble, performance, configuration and capacity management.'},
				
			   'tr29': {'td2': 'Third Party Management',
					   	'td3': 'Refers to the management and coordination of the activities of all Third Party Vendors where the Third Party Vendor provides services in direct support of the Network impacting the performance or availability of the Network. Notifying Client and the Third Party Vendor of each Third Party Vendor failure to perform in accordance with the provisions of its agreement.'},
					   
			},
						
			serviceDesk : {
						//1
						'tr1': {'td1': 'General Definitions:',
							   	'td2': 'Incident',
							   	'td3': 'An Incident is an event which is not part of the standard operation of the service and which can causes an interruption or a reduction of the quality of the service. Incidents per month are the average number of incidents or tickets recorded in the Service Desk problem tracking tool. The count of open service desk trouble tickets. ' },
			   
					   //2
					   	'tr2': {'td1': 'Q Settings & definitions:',
					   			'td2': 'Multilingual? Yes / No',
					   			'td3': 'If selected the price assumes a mix of complexity in languages: 50% standard, 35% premium and 15% complex.<br>'+
									   'Complex languages: For Europe, North America, and Latin America: Dutch, Flemish, Swedish, Danish, and Finnish are considered Complex. For AsiaPac: Mandarin, Cantonese, Japanese, Thai and Korean are considered as Complex languages.<br>'+
									   'Premium languages: For Europe, North America, and Latin America: Portuguese (except Brazil), Czech, Greek, Russian, Slovakian, Polish, and Hungarian are considered Premium. For AsiaPac: French, Spanish, Portuguese, German, Italian, Czech, Greek, Russian, Slovakian, Polish and Hungarian are considered as Premium Languages <br>'+
									   'Standard languages For North America: English, French, and Spanish are considered Standard. For Latin America: Spanish and English are considered Standard For Europe: English, French, German, Italian, and Spanish are considered Standard. For AsiaPac: English, Hindi, Tamil, Telugu, Kannada, Bengali, Malayalam, Punjabi, Marathi and Gujarati are considered Standard.'},
					   
					   'tr3': {'td2': 'Offshore Allowed? Yes / No',
							   'td3': 'If selected the price will be based on deals that include offshore (note that the term offshore is inclusive of all typical offshore areas including Europe. Benchmark data will assume 90% delivery from Poland.'},
							   
					   'tr4': {'td2': 'Level of Service? High / Medium / Low',
							   'td3': 'High: 24 x 7 hours of operation; 85% first call resolution; average handling time of 8 minutes.<br>'+
									   'Medium: 12 x 7 hours of operation; 80% first call resolution; average handling time of 10 minutes.<br>'+
									   'Low: 9 x 5 hours of operation; 70% first call resolution; average handling time of 12 minutes.<br>'+
									   'First call resolution:  the percentage of resolvable calls that are fixed and closed out on the first call (a resolvable call excludes calls that typically cannot be resolved over the phone - for example, onsite HW break-fix).<br>'+
									   'Average handling time:  the monthly mean period measurement of effort, a service desk agent spends on individual contact 1) evaluating and resolving incidents and 2) conducting incident wrap-up activities (documenting resolution, closing out the problem etc.)'},
									
					   'tr5': {'td2': 'Include Tooling & monitoring? Yes / No',
						   	   'td3': 'If selected ‘Yes’ then includes the following:<br>'+
						   		   		'Hardware (purchase and maintenance) and software (licensing and maintenance) necessary to support the following functions: problem management systems, automated problem resolution, self-help web development, remote control tools, online chat support, systems integration (asset management, network monitoring, procurement, service request management), and other incident management (e-mail, chat, self-help).'},
						
				   		//3
					   'tr6': {	'td1': 'Included in the price (irrespective of settings):',
						   		'td2': 'Support Services',
						   		'td3': 'Includes:<br>'+
								   'Level 0 Service Desk support: include self-help functions maintained by the vendor like online frequently asked questions, user generated incidents, and training videos. Other functions considered Level 0 are automated functions like password resets, user ID administration (account set up and modification), service requests, and automated events (network outages, printer maintenance alerts, capacity warnings).<br>'+
								   'Level 1: First-line service desk support that opens, populates and tracks tickets. Performs initial problem determination and attempts to resolve problems before escalating to Level 2. Focused on collecting information for reporting incidents and service requests.<br>'+
								   'Level 2 Problem Resolution: comprised of more senior technicians that solve more complex problem tickets that could not be resolved at Level 1 - Service Desk. May include coordinating desk-side assistance and lab support for issues such as: printer jams, network issues, standard software or hardware issues. Level 2 support is typically provided by the centralized Service Desk and includes problem resolution involving investigating problems, troubleshooting issues, and resolving problems for known issues.'},
					   
					   'tr7': {	'td2': 'PMO',
						   		'td3': 'Program Management Office (PMO) relates to the ITIL Design Process which is the activity that identifies requirements and defines a solution to meet those requirements.'},
					   
						//4  
					   'tr8': { 'td1': 'Excluded from the price (irrespective of settings):',
						   		'td2': 'Transition',
								'td3': 'Transition is excluded from the individual Tower pricing and should be added as part of a Q price assessment from within the Generic Inputs screen'},
					   
						'tr9': {'td2': 'Inbound calls',
								'td3': 'Financial responsibility associated with inbound toll calls (800 number).'},
					   
					   'tr10': {'td2': 'Security Procedures',
							   'td3': 'Update, maintain and distribute customer security procedures.'},
					   //5
					   'tr11': {'td1': 'Assumptions:',
							   'td2': 'Default Settings',
							   'td3': 'As part of a simple Price Assessment, a user may enter volumes at a high level. When this is done there are two options that can be chosen that create default lower level settings that Q uses to calculate the average server prices. These options include:-<br>'+
							   		  '[include assumptions]'},
						   		 
		   			   'tr12': {'td2': 'Abandonment rate',
					  		   'td3': 'Assumed 7%. This is the percentage of calls abandoned by the service desk; contacts that are 1) customer calls or 2) when a customer queues for an available service desk agent. Used to measure the effectiveness on contact center resourcing as defined in the service level agreement.'},
		   				  		
				  	   'tr13': {'td2': 'Speed to answer',
			  		 		  'td3': 'Assumed 80% within 25 seconds. This is the measurement of time required to speak to the actual service desk person who will resolve a problem as a composite of all the times in the period, not to an operator or to a voicemail system.'},
			},
			
			application : {
				'tr1': {'td1': 'General Definitions:',
					   	'td2': 'Application Operations',
					   	'td3': ' Application operations are all activities to run an application on a given infrastructure platform, such as Monitoring.' },
					   	
			   	'tr2': {'td2': 'Application Development',
					   'td3': ' Application Development is all activities related to creation of the software for an application. Usually managed as projects and split into phases (design, coding, testing etc.).'},
			   //2
			   'tr3': {'td1': 'Q Settings & definitions:',
		   				'td2': 'Offshore Allowed? Yes / No',
					   'td3': 'If selected the price will be based on deals that include offshore (note that the term offshore is inclusive of all typical offshore areas including Europe. Benchmark data will assume 80% delivery from India.'},
					   
			   'tr4': {'td2': 'Level of Service? High / Medium / Low',
					   'td3': 'High: Assumes high business critical applications and includes preventive maintenance.<br>'+
	   							'Medium: Assumes medium business critical applications and includes preventive maintenance.<br>'+
						   		'Low: Assumes low business critical applications, and excludes preventive maintenance.'},
							   
			   'tr5': {'td2': 'Simple Application',
				   	   'td3': 'Architecture is simple and traditional. Application Services has significant experience in this type of architecture limited and well established technology components. Integration is not an issue. Application Services is familiar with the technology (COTS).'},
				   		   		
	   		   	'tr6': {'td2': 'Medium Application',
				   	   'td3': 'Architecture is of simple or average complexity. Application Services has limited experience in this type of architecture multiple technology components. Application Services has proven expertise with this technology mix (COTS).'},
							   		   		
	   		   	'tr7': {'td2': 'Complex Application',
				   	   'td3': 'Architecture is complex / distributed multiple technology components that must all be integrated. Some of these technologies (or their integration) are new and yet unproven by Application Services (Bespoke).'},
										   		   		
	   		   	'tr8': {'td2': 'Very Complex Application',
				   	   'td3': 'Architecture is highly complex and distributed. Multiple technology components that must all be integrated. Many of these technologies (or their integration) are new and yet unproven by Application Services, E.g. Complex engineering / scientific applications (Bespoke).'},
				
		   		//3
			   'tr9': {	'td1': 'Included in the price (irrespective of settings):',
				   		'td2': '(Small) standard change requests',
				   		'td3': 'All pricing includes changes (up to 8 hrs), changing or altering the application functionality.'},
			   
			   'tr10': {'td2': 'User Acceptance Testing',
				   		'td3': 'Final testing (by the end users) to confirm whether the software meets the requirement is assumed to be included in the pricing.'},
			   
		   		'tr11': {'td2': 'Tools and Software',
	   					'td3': 'Development software and compilers for common development platforms (J2EE, J2ME, .NET, etc).'},
	   					

				//4  
			   'tr12': { 'td1': 'Excluded from the price (irrespective of settings):',
				   		'td2': 'Transition',
						'td3': 'Transition is excluded from the individual Tower pricing and should be added as part of a Q price assessment from within the Generic Inputs screen'},
			   
				'tr13': {'td2': 'Travel Expenses',
						'td3': 'Traveling from/to the client location is not included in the pricing.'},
			   
			   'tr14': {'td2': 'Application Development',
					   'td3': 'Not considered, usually flows via a non-standard change request or separate project.'},
					   
			   'tr15': {'td2': 'Application Operations',
				   		'td3': 'Application operations are all activities to run an application on a given infrastructure platform, such as Monitoring is not included in the pricing.'},
				   
			   'tr16': {'td2': 'Database Administration',
					   'td3': 'Database administration is a manpower driven IT service for mainly administration and monitoring of database instances. The e.g. database server hardware and application operation is not included in this service.'},
					   
			   'tr17': {'td2': 'Incident Management',
					   'td3': 'An incident is an unplanned interruption to an IT service or an impact to quality of service. The objective of Incident Management is to manage all incidents over its lifecycle and to return the IT service to users within contracted service levels. This service is not included in this pricing.'},
					   
			   //5
			   'tr18': {'td1': 'Assumptions:',
					   'td2': 'Default Settings',
					   'td3': 'As part of a simple Price Assessment, a user may enter volumes at a high level. When this is done there are two options that can be chosen that create default lower level settings that Q uses to calculate the average server prices. These options include:-<br>'+
					   		  '[include assumptions]'},
				   		 
   			   'tr19': {'td2': 'Maturity Level of Client (CMMI)',
			  		   'td3': 'Is assumed to very between CMMI level 3 and 4 depending on the Service Level setting. Higher service levels assume to have a higher CMMI requirement.'},
   				  		
	  	   },
						
		};
		
		return assumptions;
	});