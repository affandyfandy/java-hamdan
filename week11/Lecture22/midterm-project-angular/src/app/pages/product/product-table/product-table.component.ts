import { Component, OnInit } from '@angular/core';
import { faArrowUp, faArrowDown, faSort } from '@fortawesome/free-solid-svg-icons';
import { Product } from '../../../models/product.model';
import { ProductService } from '../../../services/product.service';
import { Router } from '@angular/router';
import { FormsModule } from '@angular/forms';
import { CommonModule } from '@angular/common';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { ProductFormComponent } from '../product-form/product-form.component';

@Component({
  selector: 'app-product-table',
  standalone: true,
  imports: [FormsModule, CommonModule, FontAwesomeModule, ProductFormComponent],
  templateUrl: './product-table.component.html',
})
export class ProductTableComponent implements OnInit {
  faArrowUp = faArrowUp;
  faArrowDown = faArrowDown;
  faSort = faSort;

  products: Product[] = [];
  filteredItems: Product[] = [];
  searchTerm: string = '';
  sortColumn: string = 'name';
  sortDirection: 'asc' | 'desc' = 'asc';
  isModalVisible: boolean = false;
  selectedProduct: Product | null = null;

  // Pagination properties
  currentPage: number = 1;
  itemsPerPage: number = 10;

  constructor(
    private productService: ProductService,
    private router: Router
  ) { }

  ngOnInit(): void {
    this.loadProducts();
  }

  loadProducts(): void {
    this.productService.getProducts().subscribe((products) => {
      this.products = products;
      this.filterProducts();
    });
  }

  openAddProductModal(): void {
    this.selectedProduct = null;
    this.isModalVisible = true;
  }

  openEditProductModal(product: Product): void {
    this.selectedProduct = product;
    this.isModalVisible = true;
  }

  handleSave(product: Product): void {
    if (this.selectedProduct) {
      const index = this.products.findIndex(p => p.id === this.selectedProduct!.id);
      this.productService.updateProduct(product).subscribe((result) => {
        this.products[index] = result;
        this.filterProducts();
      });
    } else {
      this.productService.addProduct(product).subscribe(() => {
        this.loadProducts();
      });
    }
    this.isModalVisible = false;
  }

  handleCancel(): void {
    this.isModalVisible = false;
  }

  sortBy(column: keyof Product): void {
    this.sortColumn = column;
    this.sortDirection = this.sortDirection === 'asc' ? 'desc' : 'asc';
    this.filterProducts();
  }

  filterProducts(): void {
    this.filteredItems = this.products
      .filter(item => item.name.toLowerCase().includes(this.searchTerm.toLowerCase()))
      .sort((a, b) => {
        const aValue = (a as any)[this.sortColumn];
        const bValue = (b as any)[this.sortColumn];
        const comparison = aValue < bValue ? -1 : 1;
        return this.sortDirection === 'asc' ? comparison : -comparison;
      })
      .slice((this.currentPage - 1) * this.itemsPerPage, this.currentPage * this.itemsPerPage);
  }

  toggleItemStatus(item: Product): void {
    item.status = item.status === 'ACTIVE' ? 'INACTIVE' : 'ACTIVE';
    this.productService.updateProduct(item).subscribe();
  }

  changePage(page: number): void {
    this.currentPage = page;
    this.filterProducts();
  }

  removeProduct(id: number): void {
    if (confirm('Are you sure you want to delete this product?')) {
      this.productService.deleteProduct(id).subscribe(() => {
        this.products = this.products.filter(item => item.id !== id);
        this.filterProducts();
      });
    }
  }
}
