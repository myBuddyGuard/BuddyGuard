export interface RoutePoint {
  latitude: number;
  longitude: number;
}

interface RouteSnapshotConfig {
  canvas: {
    width: number;
    height: number;
    paddingX: number;
    paddingY: number;
    bgColor: string;
  };
  grid: {
    enabled: boolean;
    color: string;
    gap: number;
    width: number;
  };
  line: {
    color: string;
    width: number;
  };
}

const DEFAULT_CONFIG: RouteSnapshotConfig = {
  canvas: {
    width: 600,
    height: 600,
    paddingX: 60, //600 * 0.1
    paddingY: 60,
    bgColor: '#f0f0f0',
  },

  line: { color: '#FFAE00', width: 3 },
  grid: { enabled: true, color: '#cccccc', gap: 20, width: 0.5 },
} as const;

interface RouteSnapshotOptions {
  canvasRef: React.MutableRefObject<HTMLCanvasElement> | null;
  routes: RoutePoint[];
  config?: Partial<RouteSnapshotConfig>;
}

export default class RouteSnapshot {
  private readonly config: RouteSnapshotConfig;
  private readonly canvasElement: HTMLCanvasElement;
  private readonly ctx: CanvasRenderingContext2D;
  private readonly routes: RoutePoint[];

  constructor(options: RouteSnapshotOptions) {
    if (!options.canvasRef?.current) {
      throw new Error('Canvas reference is required');
    }
    if (!options.routes.length) {
      throw new Error('No routes provided');
    }

    this.canvasElement = options.canvasRef.current;
    this.routes = options.routes;
    this.config = {
      canvas: { ...DEFAULT_CONFIG.canvas, ...options.config?.canvas },
      grid: { ...DEFAULT_CONFIG.grid, ...options.config?.grid },
      line: { ...DEFAULT_CONFIG.line, ...options.config?.line },
    };

    const ctx = this.canvasElement.getContext('2d');
    if (!ctx) throw new Error('Failed to get canvas context');
    this.ctx = ctx;

    this.initCanvas();
  }

  private initCanvas(): void {
    const { width, height } = this.config.canvas;
    this.canvasElement.width = width;
    this.canvasElement.height = height;
  }

  private getRouteBounds() {
    const latitudes = this.routes.map(({ latitude }) => latitude);
    const longitudes = this.routes.map(({ longitude }) => longitude);

    const latMin = Math.min(...latitudes);
    const latMax = Math.max(...latitudes);
    const lngMin = Math.min(...longitudes);
    const lngMax = Math.max(...longitudes);

    return { latMin, latMax, lngMin, lngMax };
  }

  private drawRoute() {
    const { latMin, latMax, lngMin, lngMax } = this.getRouteBounds();
    const {
      width: canvasWidth,
      height: canvasHeight,
      paddingX: canvasPaddingX,
      paddingY: canvasPaddingY,
    } = this.config.canvas;
    const { color: lineColor, width: lineWidth } = this.config.line;

    this.ctx.beginPath();
    this.ctx.strokeStyle = lineColor;
    this.ctx.lineWidth = lineWidth;

    this.routes.forEach((point, index) => {
      const x = canvasPaddingX + ((point.latitude - lngMin) / (lngMax - lngMin)) * (canvasWidth - 2 * canvasPaddingX);
      const y =
        canvasHeight -
        canvasPaddingY -
        ((point.latitude - latMin) / (latMax - latMin)) * (canvasHeight - 2 * canvasPaddingY);

      if (index === 0) this.ctx.moveTo(x, y);
      else this.ctx.lineTo(x, y);
    });

    this.ctx.stroke();
  }

  private drawGrid(): void {
    const { width: canvasWidth, height: canvasHeight } = this.config.canvas;
    const { gap, color, width: lineWidth } = this.config.grid;

    this.ctx.strokeStyle = color;
    this.ctx.lineWidth = lineWidth;

    for (let x = 0; x <= canvasWidth; x += gap) {
      this.ctx.beginPath();
      this.ctx.moveTo(x, 0);
      this.ctx.lineTo(x, canvasHeight);
      this.ctx.stroke();
    }

    for (let y = 0; y <= canvasHeight; y += gap) {
      this.ctx.beginPath();
      this.ctx.moveTo(0, y);
      this.ctx.lineTo(canvasWidth, y);
      this.ctx.stroke();
    }
  }

  private fillCanvasBackground(): void {
    const { bgColor, width, height } = this.config.canvas;
    this.ctx.fillStyle = bgColor;
    this.ctx.fillRect(0, 0, width, height);
  }

  generate(): string | void {
    try {
      this.fillCanvasBackground();
      if (this.config.grid.enabled) {
        this.drawGrid();
      }
      this.drawRoute();

      return this.canvasElement.toDataURL('image/png');
    } catch (error) {
      console.error('Failed to generate route snapshot:', error);
    }
  }
}
