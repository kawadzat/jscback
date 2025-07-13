export interface Project {
  id?: number;
  name: string;
  description?: string;
  code?: string;
  startDate: Date;
  endDate?: Date;
  actualStartDate?: Date;
  actualEndDate?: Date;
  status: ProjectStatus;
  priority: ProjectPriority;
  type: ProjectType;
  budget?: number;
  actualCost?: number;
  progressPercentage?: number;
  projectManagerId?: number;
  projectManager?: User;
  clientId?: number;
  client?: User;
  teamMemberIds?: number[];
  teamMembers?: User[];
  location?: string;
  department?: string;
  tags?: string;
  riskLevel?: RiskLevel;
  riskDescription?: string;
  qualityScore?: number;
  customerSatisfactionScore?: number;
  isActive?: boolean;
  isTemplate?: boolean;
  parentProjectId?: number;
  notes?: string;
  completionCriteria?: string;
  successMetrics?: string;
  
  // Computed fields
  daysRemaining?: number;
  isOverdue?: boolean;
  isOnTrack?: boolean;
  budgetUtilization?: number;
  statusColor?: string;
  priorityColor?: string;
  
  // Audit fields
  createdBy?: string;
  createdAt?: Date;
  lastModifiedBy?: string;
  lastModifiedAt?: Date;
}

export enum ProjectStatus {
  PLANNING = 'PLANNING',
  ACTIVE = 'ACTIVE',
  ON_HOLD = 'ON_HOLD',
  COMPLETED = 'COMPLETED',
  CANCELLED = 'CANCELLED',
  SUSPENDED = 'SUSPENDED',
  REVIEW = 'REVIEW',
  APPROVED = 'APPROVED',
  REJECTED = 'REJECTED',
  ARCHIVED = 'ARCHIVED'
}

export enum ProjectPriority {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
  CRITICAL = 'CRITICAL',
  URGENT = 'URGENT'
}

export enum ProjectType {
  DEVELOPMENT = 'DEVELOPMENT',
  MAINTENANCE = 'MAINTENANCE',
  RESEARCH = 'RESEARCH',
  INFRASTRUCTURE = 'INFRASTRUCTURE',
  MIGRATION = 'MIGRATION',
  UPGRADE = 'UPGRADE',
  CONSULTING = 'CONSULTING',
  TRAINING = 'TRAINING',
  DOCUMENTATION = 'DOCUMENTATION',
  TESTING = 'TESTING',
  DEPLOYMENT = 'DEPLOYMENT',
  INTEGRATION = 'INTEGRATION',
  CUSTOMIZATION = 'CUSTOMIZATION',
  SUPPORT = 'SUPPORT',
  OTHER = 'OTHER'
}

export enum RiskLevel {
  LOW = 'LOW',
  MEDIUM = 'MEDIUM',
  HIGH = 'HIGH',
  CRITICAL = 'CRITICAL'
}

export interface User {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  title?: string;
  department?: string;
  imageUrl?: string;
}

export interface ProjectSearchCriteria {
  status?: ProjectStatus;
  priority?: ProjectPriority;
  type?: ProjectType;
  department?: string;
  searchTerm?: string;
  managerId?: number;
  teamMemberId?: number;
  isActive?: boolean;
}

export interface ProjectStats {
  totalProjects: number;
  activeProjects: number;
  completedProjects: number;
  overdueProjects: number;
  highRiskProjects: number;
  totalBudget: number;
  totalActualCost: number;
  averageProgress: number;
} 